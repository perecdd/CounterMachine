Counter machine - there is a four (Q, n, P, q0), where Q is the set of states, n is the number of counters, P is the program of the machine and q0 is the initial state.
The program P is a set of commands of the form q -> inc(i) p and q -> dec(i) p1 : p2, here q, p, p1, p2 are the states of the counter machine, i is the counter number.

In this implementation of the machine, they all represent a word in the English alphabet consisting of numbers and letters in two registers. In addition, for ease of use, commands can be combined into functions and used in practice prepared blocks of increments and decrements.

## A detailed description of the language as a programming language.

All functions are declared the same type:

#name(initial state)(counters that will be accessible from the outside) final states {code block}.

Each function must have an initial state, and only one (this program is an emulator of a deterministic Minsky machine).

~~~
% Use '%' to comment
% In this case, q0 is the initial state
#MyFirstFunction(q0)() qE {
% do something
}
~~~

Counters do not have to be specified (although this is strange, because the function will not depend on the outside world), if necessary, the names are separated by a colon, the number of spaces does not matter.

~~~
% A function that does nothing :)
#MyFirstFunction(q0)() qE {
% do something
}
% Uses (I want to believe it) 5 counters for its calculations.
#MyFirstFunction(q0)(1 : 2 : 3 : 4 : 5) qE {
% do something
}
~~~

The final states must be specified to all functions except main.

~~~
#main(q0)(){
% do something
}
~~~

This label is necessary so that the programmer can understand which label to consider as the final one. For the user, only the number of these labels and their meaning matter. They are also indicated by a colon. In the code block, you write a set of commands separated by a semicolon. You can divide them line by line.

~~~
% If Counter1 is equal to 0, then the transition will be made to the label (state) 2, otherwise 1.
#MyFirstFunction(q0)(Counter1) 1 : 2 {
	q0 -> dec(Counter1) 1 : 2;
}
% Calling the function on the first counter.
#main(q0)(){
	q0 -> MyFirstFunction(1) q1 : q2;
	% It is important that when calling the function,
	% the user sets the final labels himself and substitutes the counters himself.
}
~~~

It is also worth noting that in order to use functions, you need to declare them in advance, before using them (even in functions other than main).

~~~% If Counter1 is equal to 0, then the transition will be made to the label (state) 2, otherwise 1.
% Bad program
#main(q0)(){
	q0 -> MyFirstFunction(1) q1 : q2;
}
#MyFirstFunction(q0)(Counter1) 1 : 2 {
	% do something
}
~~~

~~~
% Good program
#MyFirstFunction(q0)(Counter1) 1 : 2 {
	% do something
}
#main(q0)(){
	q0 -> MyFirstFunction(1) q1 : q2;
}
~~~

## Using the preprocessor

The preprocessor supports two types of $ commands - connect the library (the same file with the same functions, only without main). % - for comments.

~~~
% lib.txt
#zero(S)(N) E{
	S -> dec(N) S : E;
}
~~~

~~~
% main.txt
$lib.txt

#main(S)(){
	S -> zero(2) E;
}
~~~

## How to start programming

This language has a low entry threshold and is necessary primarily for writing training programs and mastering the theory of algorithms, the most important thing is to understand the concept of counter machines.

## Where to find code examples

Code examples and a "library" with simple code examples can be found in "./example" and "./libs", respectively.