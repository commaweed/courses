# python 3

* everything in Python is an object

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
```

## Numbers

| integral | non-integral |
| ---- | ---- |
| Integers | Floats (c doubles) |
| Booleans | Complex |
|  | Decimals |
|  | Fractions |

## Collections

| Sequences | Sets | Mappings |
| ---- | ---- | ---- |
| Mutable | Immutable | Mutable | Immutable |
| Lists | Tuples | Sets | Frozen Sets | Dictionaries |

## Callables

* anything you can invoke

| <!-- --> |
| ---- |
| User-Defined Functions |
| Generators |
| Classes |
| Instance Methods |
| Class Instances (__call__()) |
| Built-in Functions (e.g. len(), open()) |
| Built-in Methods (e.g. my_list.append(x)) |

## Singletons

| <!-- --> |
| ---- |
| None |
| Not Implemented |
| Ellipsis Operator (...) |

## Statements

### Python Program

| <!-- --> |
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
