## compare logs

We have two files. file1 and file2

file1 is composed of parts A and B.
file2 is composed of just part B.

The necessity is to output file3 containing part A.

Outer left join ( file1 (part A + part B + id), file2 (part B + id)  )

assumption: file1 and file2 are not ordered.

-- Steps

    - file1: Stream[String] = Consume file1 (path)
    - file2: Stream[String] = Consume file2 (path)
    
    - file1Domain : Stream[DomainObject] = file1.toDomain
    - file2Domain : Stream[DomainObject] = file2.toDomain

    - file3: Stream[DomainObject] = file1Domain filterWith file2Domain
    - write ouput(file3, targetPath)


### motivations:
I know it does not have data to run it, you can build the model of your data (with its own codecs) 
and just run the program with it. 

This project is intended to get familiar with fs2 (and solve things that I actually need)
