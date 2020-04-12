program to compare two directories ("left" and "right") and shows the file differences between them (identical, left only, right only, conflict).

files are assumed to be identical if they have the same size. 

it does not open the files to compare the contents as this may be undesirable across a network. 

also it ignores the created/modified date as this might be meaningless if the files have already been copied in the past.

additionally, it does not provide any tools to manipulate the files, because
1. java is not necessarily very good at preserving os-level file attributes
2. you might want to automate resolving the differences (e.g. using a program like robocopy on windows)
