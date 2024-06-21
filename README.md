## object-in-logs-comparer

I have two files containing **logs** of `serialized objects`.

file1 is the output merge `A` and `B`.
file2 is only `B` output.

The need is: file3 containing `A`.

assumption: 
- file1 and file2 are not ordered. therefore I will store it in memory until I hash it and use some sort of Id

-- Steps

    - file1: Stream[String] = Consume file1 (path)
    - file2: Stream[String] = Consume file2 (path)
    
    - file1Domain : Stream[DomainObject] = file1.toDomain
    - file2Domain : Stream[DomainObject] = file2.toDomain

    - file3: Stream[DomainObject] = file1Domain filterWith file2Domain
    - write ouput(file3, targetPath)


### motivations:
I know that making it a list breaks the purpose of a streaming but I wanted to gain familiarity with fs2 AND solve the problem, this is one way I found.
