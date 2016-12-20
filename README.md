# RingBufferOnePubManySub

This is a quick-and-dirty project I threw together to demonstrate how to apply a particular
data structure (a ring buffer) to a problem (graphing data coming from multiple sensors).
I recommended this data structure to a friend because his sensors emit 1000 measurements per
second and he was really thrashing his heap because he was allocating and then discarding
multiple variables for each measurement.

If this were a real data structure or code intended for production I would have written
tests and avoided using so many single-letter variable names.
