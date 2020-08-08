# python 3

* everything in Python is an object
* there are no static types in Python; all variables and function return types are by default dynamic

```python
# even operations get converted into object "dot" notation by the Python interpretter
x < y # -> x.__lt__(y)
```

## comments

```python
# this is a single line or inline comment

"""
This is a multiline string and when placed just under a function definition, it serves as a comment of sorts (associated docstring).
"""
```

## variables

### identifier names

* they are case-sensitive
* must start with (_) or letter (a-z or A-Z), then can be followed by any number of underscores (_), letters (a-z or A-Z), or digits (0-9)
* cannot be a reserved word (e.g. None, True, break, def)

### conventions

* see pep 8 Style guide
* they don't have to be followed

```python
# private variables (implies internal use because true private encapsulation isn't part of python)
# objects names this way will not get imported by statements like:  "from module import *"
_my_var

# to mangle class attributes - useful in inheritance chains
__my_var

# system-defined names - have a special meaning to the interpretter
# don't invent new ones; stick to the ones pre-defined by Python
__init__
x < y # same as x.__lt__(y)

# packages (short, all-lowercase, no-underscores)
utilities

# Modules (short, all-lowercase, can use underscores)
db_utils

# Classes (camelcase)
BankAccount

# variables (lowercase, words separated by underscore)
account_id

# constants (uppercase, words separated by underscore)
MIN_APR

# functions (lowercase, words separated by underscore)
open_account
```

## Numbers

| integral | non-integral |
| ---- | ---- |
| Integers | Floats (c doubles) |
| Booleans | Complex |
|  | Decimals |
|  | Fractions |

## Strings

### multiline strings

```python
# multiline strings are not comments, but can be used as such (e.g. docstrings) to define function/method blocks
'''This is a multiline string
	all whitespace including newline and tab are preserved as written.
'''

a = """
Can also use double quotes 
"""
```

## Collections

| Sequences |  | Sets || SetsMappings|
| ---- | ---- | ---- | ---- | ---- |
| Mutable | Immutable | Mutable | Immutable | Mutable |
| Lists | Tuples | Sets | Frozen Sets | Dictionaries |

## Callables

* anything you can invoke

| :Type |
| ---- |
| User-Defined Functions |
| Generators |
| Classes |
| Instance Methods |
| Class Instances (__call__()) |
| Built-in Functions (e.g. len(), open()) |
| Built-in Methods (e.g. my_list.append(x)) |

## Singletons

| :Type |
| ---- |
| None |
| Not Implemented |
| Ellipsis Operator (...) |

## Statements

### Python Program

| :Type |
| ---- |
| phyical lines of code | end with a physical newline character|
| logical lines of code | end with a logical NEWLINE token|
| tokenized |

* there is a difference between physical newline vs logical newline
* sometimes physical newlines are ignored
* in order to combine multiple physical lines into a single logical line of code, must terminate with a logical NEWLINE token
* conversion can be done implicitly or explicitly

### expressions that allow for implicit multiline statements

```python
# list literals: []
[ 1, 2,
	3
]

# tuple literals: ()
( 
    1,
	2,
	3
)
# dictionary literals: {}
{ 	"name":"value", 
	'name2':'value1'
}

# set literals: {}
{
	"1"
	, "2"
}

# function arguments / parameters
def my_func(a,
	b, c):
	print(a,b,c)
my_func(10, #comment
	20, 30)
```

### explicit multiline statements

```python
# you can break statements over multiple lines explicitly by using the '\' 
# multiline statements are not implicitly converted to a single logical line
# WARNING: cannot put comments inside the statement; must be above or below the entire statement
if a \
	and b \
	and c:
```
## Conditionals

```python
# if/ elif / else
a = 6
if a < 5:
	print('a < 5')
elif a < 10:
	print ('5 <= a < 10')
else:
	print('a >= 5')

# conditional expression (ternary) - X if (condition is true) else Y - cannot handle blocks (singleline only)
b = 'a < 5' if a < 5 else 'a >= 5'
```

## Functions

```python
# built-in functions
s = [ 1, 2, 3 ]
print(len(s))

# imported functions
from math import sqrt
print(sqrt(4))
import math
print (math.pi)
print (math.exp(1))

# define a function / call a function
def func_1(a, b):
	print("running func_1: {}".format(a * b))
func_1(4, 5)

# interpretter will ignore declared types (just for readability)
def func_2(a: int, b: int):
	return a * b
func_2('a', 3) # 'aaa'
func_2([1, 2], 3)  # [1, 2, 1, 2, 1, 2]
func_2('a', 'b') # doesn't work because multiplication doesn't exist for strings
```
