CaptureLabel
============
This is a (essentially) a JLabel that displays the contents of an OutputStream
as a String. This allows you to redirect standard output to the JLabel, and then
display it line by line.

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

Running
-------
The `MainApplication` class shows a simple example of using the CaptureLabel component
in an application to capture data sent to System.out or System.err. To add a CaptureLabel,
instantiate the object and add it to the `JFrame` as you desire; in the example application
I've attached it to the bottom of my main window.

To actually recieve and consume data, you need to create a CaptureStream and have data fed into
it. This is achieved in the example application by wrapping it in a `PrintStream`and then calling
`System.setOut` to redirect the standard output to the CaptureStream.


```java
//In the parent window, create and add label
CaptureLabel<String> label = new CaptureLabel<>();
getContentPane().add(label,BorderLayout.SOUTH);
//Attach to a CaptureStream, with prefix 'STDOUT' and the normal System.out as the 
// alternative stream
CaptureStream stream = new CaptureStream("STDOUT",label,System.out);
//Direct messages to the Capture Stream
System.setOut(stream);
```
