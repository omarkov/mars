## -*- Mode: Python -*-
#
# Copyright (c) 2004, Oliver Markovic <entrox@entrox.org> 
#   All rights reserved. 
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are met:
#
#  o Redistributions of source code must retain the above copyright notice,
#    this list of conditions and the following disclaimer. 
#  o Redistributions in binary form must reproduce the above copyright
#    notice, this list of conditions and the following disclaimer in the
#    documentation and/or other materials provided with the distribution. 
#  o Neither the name of the author nor the names of the contributors may be
#    used to endorse or promote products derived from this software without
#    specific prior written permission. 
#
# THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
# AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
# IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
# ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
# LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
# CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
# SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
# INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
# CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
# ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
# POSSIBILITY OF SUCH DAMAGE.



# -----------------------------------------------------------------------------
# Generic marshalling and unmarshalling of primitive types
# -----------------------------------------------------------------------------

def marshall(thing):
    dispatch_table = \
	{ bool: marshall_bool,
	  int: marshall_numeric,
	  str: marshall_string,
	  list: marshall_list}

    try:
	return dispatch_table[type(thing)](thing)
    except KeyError:
	raise RuntimeError, "Can't marshall " + str(type(thing))

def unmarshall(type, value):
    dispatch_table = \
	{ "BOOL": unmarshall_bool,
	  "NUMERIC": unmarshall_numeric,
	  "STRING": unmarshall_string,
	  "LIST": unmarshall_list}

    try:
	return dispatch_table[type](value)
    except KeyError:
	raise RuntimeError, "Unknown type " + str(type)


# BOOL <-> boolean

def marshall_bool(b):
    return str(b).lower()

def unmarshall_bool(str):
    if str == "true":
	return True
    elif str == "false":
	return False
    else:
	raise RuntimeError, "\"" + str + "\" can't be unmarshalled to boolean"


# NUMERIC <-> int

def marshall_numeric(i):
    return str(i)

def unmarshall_numeric(str):
    return int(str)



# STRING <-> str

def marshall_string(str):
    result = str.replace(" ", "<<SPACE>>")
    result = result.replace("\n", "<<NEWLINE>>")
    return result

def unmarshall_string(str):
    result = str.replace("<<SPACE>>", " ")
    result = result.replace("<<NEWLINE>>", "\n")
    return result


# LIST <-> list

def marshall_list(list):
    if len(list) > 0:
	return "<<SEP>>".join(map(marshall_string, list))
    else:
	return "<<EMPTY>>"

def unmarshall_list(str):
    if len(str) > 0 and str.find("<<EMPTY>>") == -1:
	return map(unmarshall_string, str.split("<<SEP>>"))
    else:
	return []
