@rem Copyright 2011 Levi Hoogenberg
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem http://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.

@echo off

if '%ALUMINUM_HOME%' == '' goto no_aluminum_home
if '%JAVA_HOME' == '' goto no_java_home

"%JAVA_HOME%\bin\java" -cp "%ALUMINUM_HOME%\lib\*" com.googlecode.aluminumproject.cli.commands.aludoc.AluDoc %*
goto end

:no_aluminum_home
echo Environment variable ALUMINUM_HOME is not set - please set it to the directory where you installed Aluminum.
goto end

:no_java_home
echo Can't find Java - please set the JAVA_HOME environment variable.
goto end

:end