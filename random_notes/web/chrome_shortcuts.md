### Commands to use with the source tab

| command | Description | 
| ---- | ---- |
| shift-ESC | visit the task manager |
| ESC | toggle console / close settings dialog |
| F2 | toggle edit as HTML |
| F12 | open developer tools |
| h | hide element |
| ctrl-d | shows next occurrence of selected variable |
| ctrl-f | Text search within current file or panel |
| ctrl-g | goto line |
| ctrl-l | clear console |
| ctrl-m | goto matching bracket |
| ctrl-o | Search by filename (except on Timeline) |
| ctrl-p | file selector dialog box |
| ctrl-p:# | jump to line number (type :55 for example in box) |
| ctrl-r / F5 | Refresh the page |
| ctrl-u | Undo last selection |
| ctrl-+ | Zoom in (while focused in DevTools) |
| ctrl— | Zoom out (while focused in DevTools) |
| ctrl-0 | Restore default text size |
| ctrl-. | Select next call frame (debug break) |
| ctrl-, | Select previous call frame (debug break) |
| ctrl-/ | comment a line |
| ctrl-ENTER | run a snippet |
| ctrl-shift-f | Text search across all sources |
| ctrl-shift-p | function selector dialog box for current file |
| ctrl-shift-o | Go to member |
| ctrl-shift-r / ctrl-F5 | Refresh the page ignoring cached content |
| shift-ENTER | Multi-line entry |

### element selectors

| Selector | Description | Example |
| ---- | ---- | ---- |
| $_ | Returns the value of the most recently evaluated expression. | > 2+2 -> 4 > _$ -> 4 |
| $0 - $4 | Dev Tools remembers the last five DOM elements (or JavaScript heap objects) that you’ve selected in the tab. | select then do copy($0) -> copy html contents to clipboard |
| $(selector) | Returns reference to the first DOM element with the specified CSS selector; alias for document.querySelector() | $(‘img’).src |
| $$(selector) | Returns an array of elements that match the CSS selector; alias for document.querySelectorAll() | $$(‘ol.toc li) then do $_.length to get array length |
| document.querySelector(‘.class’) | see $(selector) 	 | | 
| document.querySelectorAll(‘a’) | see $$(selector) 	 | | 
| $x(path) | Returns an array of DOM elements that match the given XPath expression. | $x(“//p[a]”) -> return all P elements that contain a elements |

### different ways to use console

| console function | Description |
| ---- | ---- |
| console.assert(expression, object) | if expression = false, message/stacktrace is written to console (message = object) |
| console.count(label) | to determine how many times something has been called |
| console.clear() | clears the console history (ctrl-l) |
| console.debug(object[, object, ...]) | same as console.log() |
| console.dir() | Displays an object-style listing of all the properties of the specified object. |
| console.dirxml(object) | Prints an XML representation of the specified object, as seen in the Elements tab. |
| console.group(object[, object, ...]) | Starts a new logging group with optional title; end with console.groupEnd() |
| console.groupCollapsed(‘x’) | starts a new logging group initially collapsed; end with , console.groupEnd(‘x’) |
| console.log(‘one’, variable, ‘two’, variable2); | use , instead of + |
| console.profile([label]); | starts JS CPU profile with optional label; console.profileEnd() to end it |
| console.table(object or array) | Makes a pretty table |
| console.time([label]) | start a timer with associated optional label; end with console.timeEnd([label]) |
| console.timeStamp(‘please be super fast, k’); | adds a timestamp to output - similar to log4j |
| console.trace() | prints a stacktrace from the point where this function was called |
| dir($(‘#id’)) | alias for console.dir() |
| dirxml($(‘#id’)) | alias for console.dirxml() |
| debugger | the global debugger function placed within your code causes Chrome to stop progrm exectuion and start a debugging session |

### javascript functions

| Selector | Description | Example |
| ---- | ---- | ---- |
| clear() | clears the console 	 | | 
| copy(object) | Copies a string representation of the specified object to the clipboard. | select then copy($0) |
| debug(function) | when the function specified is called, breaks inside the function | undebug(function) to turn off |
| inspect(object/function) | Opens and selects the specified element or object in the appropriate panel | inspect(document.body.firstChild); |
| getEventListeners(object) | Returns the event listeners registered on the specified object. | getEventListeners(document); |
| JSON.stringify(object) | Turns the object ingo JSON (if it can). | | 
| keys(object) | Returns an array containing the names of the properties belonging to the specified object. 	 | | 
| values(object) | Returns an array containing the values belonging to the specified object. 	 | | 
| monitor(function) | When function is called, a message is logged to the console with function name and arguments | function sum(x, y) {return x + y;} monitor(sum); |
| unmonitor(function) | To cease monitoring function 	 | | 
| monitorEvents(object[, events]) | When one of the specified events occurs on the specified object, the Event object is logged to the console | monitorEvents(window, [“resize”, “scroll”]) |
| unmonitorEvents(object) | To cease monitoring events 	 | | 
| profile([name]) | Starts a JavaScript CPU profiling session with an optional name. | complete with profileEnd() |
| table(data[, columns]) | Log object data with table by passing in a data object in with optional column headings. | complete with profileEnd() |
| document.body.contentEditable=true | allows you to edit the html directly (like using word) | | 

### javascript / DOM events

| Event type | Corresponding mapped events |
| ---- | ---- |
| control | “resize”, “scroll”, “zoom”, “focus”, “blur”, “select”, “change”, “submit”, “reset” |
| css | “animationend”, “animationiteration”, “animationstart”, “pagehide”, "pageshow", "resize", "scroll", "transitioncancel", "transitionend", "transitionstart" |
| focus | “blur”, “focus”, “focusin”, “focusout” |
| form | “change”, “input”, “invalid”, “reset”, “select”, “submit”, “compositionend”, “compositionstart”, “compositionupdate" |
| key | “keydown”, “keyup”, “keypress”, “textInput” |
| load | “abort”, “beforeunload”, “DOMContentLoad”, “error”, “load”, “readystatechange”, “unload” |
| mouse | “click”, “dblclick”, “mousedown”, “mouseeenter”, “mouseleave”, “mousemove”, “mouseout”, “mouseover”, “mouseup”, “mouseleave”, “mousewheel” |
| touch | “touchstart”, “touchmove”, “touchend”, “touchcancel” |

### notes
* if the network tab is slow in Chrome, you can disable hardware acceleration using the following steps
1.  click the settings icon in the top-right of the browser window (hamburger symbol)
2.  choose settings
3.  click "show advanced settings..." at the bottom
4.  scroll to nearly the end and there is a check box called "Use hardware acceleration when available", and make sure it is NOT checked.
5.  restart the browser

### angular notes

```javascript
var handleToYourService = angular.element("#someId").injector().get('YourService');
var scopeOfMstRecentSelectedItem = angular.element($0).scope();
var handleToYourController = angular.element($0).controller(); controller.constructor.name;
var myScope = angular.element('[ng-controller=theControllerName]').scope();
$($0).scope.someScopeValue = blah; $($0).scope().$digst(); // used to re-evaluate a scope variable 

// if you want to remove angular scope circular reference stuff, you can omit the angular variables
var newScope = _.omit(scopeOfMostRecentSelectedItem, function(value, key, object) { return key.startsWith("$") || key === "this" });
```
