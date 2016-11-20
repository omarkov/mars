#!/usr/bin/perl

if(@ARGV == 0)
{
	print "\n\nUsage: $0 [Controller]\n\n";
	exit(0);
}

my $controllerFile = shift(@ARGV);
undef $/; 
open(FILE, $controllerFile);
my $controller = <FILE>;
close(FILE);

my $controllerContent = "//START_AUTO_GEN\n";

my $files = `grep START_AUTO_GEN *.java`;
$files =~ s/(.*):.*$/$1/gm;
my @files = split(/\n/, $files);

foreach $filename (@files)
{
	print $filename."\n";
	$file = &parseFile($filename, \$controllerContent);
	open(FILE, ">".$filename);
	print FILE $file;
	close(FILE);
}
$controllerContent .= "//STOP_AUTO_GEN";
$controller = &replaceAutoGen($controller, $controllerContent);
open(FILE, ">".$controllerFile);
print FILE $controller;
close(FILE);

sub parseFile
{
	my $filename = shift;
	my $controller = shift;

	undef $/; 
	open(FILE, $filename);
	my $file = <FILE>;

	my $t = &removeAutoGen($file);

	my $content = "//START_AUTO_GEN\n\tpublic ModuleDescription addCommands(ModuleDescription module) {\n";

	LOOP: while($t =~ s/((\/\/([\w,;]+).{0,2})?(^\s+public.+?\{))//ms)
	{
		if($1 =~ /static/)
		{
			next LOOP;
		}
		my @listTypes = split(/[;,]/,$3);
		my $p   = $4;
		$p =~ s/.*?(\w+)\s+(\w+)\s*\((.*?)\).*/$3/ms;
		my @p = split(/,/, $p);
		my $ret = $1;
		my $fkt = $2;

		my $listType = "";
		if($ret =~ /List/)
		{
			$listType = shift(@listTypes);
		}

		($retP, $nestedType) = &getParam($ret, $listType);

		my $mod = "";
		my $show = 1;
		my $controller_body = "";
		my $controller_t = "";

		if($retP ne "" && $retP ne "ERR")
		{
			if($nestedType ne "")
			{
				$mod .= "\t\tCommand $fkt = new Command(\"$fkt\", new $retP($nestedType.class), this);\n\n";
			}else
			{
				$mod .= "\t\tCommand $fkt = new Command(\"$fkt\", new $retP(), this);\n\n";
			}
			$controller_t .= "\tpublic $ret $fkt(";
			$controller_body .= "\t\treturn ($ret)net.call(\"system\", \"$fkt\", new Object[]{";
		}elsif($retP eq "ERR")
		{
			$show = 0;
		}else
		{
			$controller_t .= "public void $fkt(";
			$controller_body .= "\t\tnet.call(\"system\", \"$fkt\", new Object[]{";
			$mod .= "\t\tCommand $fkt = new Command(\"$fkt\", null, this);\n\n";
		}
 		foreach(@p)
		{
			$_ =~ s/\s+/ /g;
			$_ =~ s/^\s+|\s+$//g;
			($type, $name)  = split(/ /);
			
			my $listType = "";
			if($type =~ /List/)
			{
				$listType = shift(@listTypes);
			}
			($typeP, $nestedType) = &getParam($type, $listType);
			if($typeP eq "ERR")
			{
				$show = 0;	
			}
			$controller_t .= "$type $name,";
			$controller_body .= "$name,";
			if($nestedType ne "")
			{
				$mod .= "\t\t$fkt.addParameter(new $typeP(\"$name\", $nestedType.class));\n";
			}else
			{
				$mod .= "\t\t$fkt.addParameter(new $typeP(\"$name\", null));\n";
			}
		}
		$controller_t =~ s/,$//;
		$controller_body =~ s/,$//;
		$controller_t .= ") throws NetworkException {\n";
		$controller_body .= "});\n";
		$controller_t .= $controller_body."\t}\n\n";

		$mod .= "\n\t\tmodule.addInterface($fkt);\n";

		if($show)
		{
			$content .= $mod;
			$$controller .= $controller_t;
		}
	}

	$content .= "\t\treturn module;\n\t}\n//STOP_AUTO_GEN";

	return &replaceAutoGen($file, $content);
}

sub replaceAutoGen
{
	my $content = shift;
	my $replace = shift;
	$content =~ s/\/\/START_AUTO_GEN.*\/\/STOP_AUTO_GEN/$replace/gms;
	return $content;	
}

sub removeAutoGen
{
	my $content = shift;
	$content =~ s/\/\/START_AUTO_GEN.*\/\/STOP_AUTO_GEN//gms;
	return $content;
}

sub getParam
{
	my $p = shift;
	my $type = shift;
	if($p =~ /set/i)
	{
		return ("ERR", "");
	}
	if($p =~ /void/i)
	{
		return ("", "");
	}
	if($p =~ /boolean/i)
	{
		return ("BooleanParameter", "");
	}
	if($p =~ /int/i)
	{
		return ("NumericParameter", "");
	}
	if($p =~ /string/i)
	{
		return ("StringParameter", "");
	}
	if($p =~ /list/i)
	{
        if($type eq "List")
        {
            return ("ObjectParameter", $type);
        }
		if($type eq ""){$type = "String";}
		return ("ListParameter", $type);
	}
	return ("ObjectParameter", $p);
}
