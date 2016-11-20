#include <sys/types.h>
#include <stdio.h>
#include <unistd.h>
#include <grp.h>
#include <pwd.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#ifdef __DARWIN__
#include <JavaVM/jni.h>
#else
#include <jni.h>
#endif


JNIEXPORT jint JNICALL
Java_server_osi_LinuxUserManager_getUIDForName(JNIEnv *env, jclass class, jstring str)
{
    const char *user_name = (*env)->GetStringUTFChars(env, str, 0);
    struct passwd *user_info = getpwnam(user_name);

    (*env)->ReleaseStringUTFChars(env, str, user_name);

    if (user_name)
	return user_info->pw_uid;
    else
	return -1;
}

JNIEXPORT jint JNICALL
Java_server_osi_LinuxUserManager_getGIDForName(JNIEnv *env, jclass class, jstring str)
{
    const char *group_name = (*env)->GetStringUTFChars(env, str, 0);
    struct group *group_info = getgrnam(group_name);

    (*env)->ReleaseStringUTFChars(env, str, group_name);

    if (group_info)
	return group_info->gr_gid;
    else
	return -1;
}

JNIEXPORT jint JNICALL
Java_server_osi_LinuxUserManager_chown(JNIEnv *env, jclass class, jstring jpath, jint owner, jint group)
{
    const char *path = (*env)->GetStringUTFChars(env, jpath, 0);
    int result;

    result = chown(path, owner, group);

    (*env)->ReleaseStringUTFChars(env, jpath, path);

    return result;
}

JNIEXPORT jint JNICALL
Java_server_osi_LinuxUserManager_chmod(JNIEnv *env, jclass class, jstring jpath, jint mode)
{
    const char *path = (*env)->GetStringUTFChars(env, jpath, 0);
    int result;

    result = chmod(path, mode);

    (*env)->ReleaseStringUTFChars(env, jpath, path);

    return result;
}

JNIEXPORT jstring JNICALL
Java_server_osi_LinuxUserManager_crypt(JNIEnv *env, jclass class, jstring str)
{
    const char *cstr = (*env)->GetStringUTFChars(env, str, 0);
    const char *salt = "OM";

    char *crypted_passwd = crypt(cstr, salt);

    (*env)->ReleaseStringUTFChars(env, str, cstr);

    return (*env)->NewStringUTF(env, crypted_passwd);
}


JNIEXPORT jint JNICALL
Java_server_osi_LinuxUserManager_symlink(JNIEnv *env, jclass class, jstring name1, jstring name2)
{
    const char *cname1 = (*env)->GetStringUTFChars(env, name1, 0);
    const char *cname2 = (*env)->GetStringUTFChars(env, name2, 0);

    int result = symlink(cname1, cname2);

    printf("symlinking %s to %s. returned %d.\n", cname1, cname2, result);

    (*env)->ReleaseStringUTFChars(env, name1, cname1);
    (*env)->ReleaseStringUTFChars(env, name2, cname2);

    return result;
}

JNIEXPORT jint JNICALL
Java_server_osi_LinuxUserManager_execAsUID(JNIEnv *env, jclass class, jstring cmd, jobjectArray args, jint uid)
{
    // get the command
    const char *command = (*env)->GetStringUTFChars(env, cmd, 0);

    // get the arguments
    jsize length = (*env)->GetArrayLength(env, args);
    char *arguments[length+2];

    // argv[0] is the command
    arguments[0] = strdup(command);

    int i;

    for (i = 0; i < length; i++) {
	jstring arg = (jstring)(*env)->GetObjectArrayElement(env, args, (jsize)i);
	const char *c_arg = (*env)->GetStringUTFChars(env, arg, 0);

	arguments[i+1] = strdup(c_arg);

	(*env)->ReleaseStringUTFChars(env, arg, c_arg);
    }

    arguments[length+1] = NULL;

    // do ze fork
    pid_t pid = fork();
    int result;

    if (pid == 0) {
        struct group *agrp = getgrnam("audio");
        if(!setgid(agrp->gr_gid))
        {
	    perror("Couldn't change grp id");
	    result = -1;
        }
	// change user id
        if (!setuid(uid)) {
	    perror("Couldn't change user id");
	    result = -1;
	}

	struct passwd *user_info = getpwuid(uid);

	if (!user_info)
	    result = -1;

	// change working directory to home
	if (!chdir(user_info->pw_dir)) {
	    perror("Couldn't change to home directory");
	    result = -1;
	}

    // set HOME
    if (setenv("HOME", user_info->pw_dir, 1)) {
        perror("Couldn't set HOME");
        result = -1;
    }

    // set DISPLAY
    if (setenv("DISPLAY", ":0", 1) == -1)
    {
	    perror("Couldn't set DISPLAY");
	    result = -1;
    }

	// execute the program
	if (execvp(arguments[0], arguments) == -1) {
	    perror("Couldn't spawn executable");
	    result = -1;
	}
    } else if (pid == -1) {
	// error
	perror("Couldn't fork process");
	result = -1;
    } else {
	// parent
	while (wait(&result) != pid);
    }

    // free ze shit
    for (i = 0; i < length; i++)
	free(arguments[i]);
    (*env)->ReleaseStringUTFChars(env, cmd, command);

    return result;
}
