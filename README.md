CaptureLabel
============
This project came about as I needed a simple way to surface a single 
line of text to the user if a debug flag was set, rather than asking
them to spin up the application in a terminal.

This collection of classes implements (adjusted for my needs) the answer
on this stack overflow question: 
[https://stackoverflow.com/q/13893655](https://stackoverflow.com/q/13893655).

My Changes
----------

*   We can set the colour to red if we are outputting stderr
*   We output to a message label, and wipe the text every time
    we write to the CaptureStream
*   We do some basic escaping on the input string (replace all the
    '<' and '>' with their HTML entities). This is more to ensure the
    display isn't messed up by being accidentally shoved into an HTML
    context, rather than to prevent attacks.
*   Mild generics