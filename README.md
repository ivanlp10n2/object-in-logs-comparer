## compare logs

We have two files. file1 and file2

file1 is composed of parts A and B.
file2 is composed of just part B.

The necessity is to output file3 composed of just part A.

assumption: file1 and file2 are not ordered.

-- Steps

    - file1: Stream[String] = Consume file1 (path)
    - file2: Stream[String] = Consume file2 (path)
    
    - file1Domain : Stream[DomainObject] = file1.toDomain
    - file2Domain : Stream[DomainObject] = file2.toDomain

    - file3: Stream[DomainObject] = file1Domain filterWith file2Domain
    - write ouput(file3, targetPath)
