/* 
This file is part of the PolePosition database benchmark
http://www.polepos.org

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public
License along with this program; if not, write to the Free
Software Foundation, Inc., 59 Temple Place - Suite 330, Boston,
MA  02111-1307, USA. */


package org.polepos.enhance;

import java.io.*;

public class IOServices {
    
	public static String buildTempPath(String fname) {
		return Path4.combine(Path4.getTempPath(), fname);
	}

	public static String safeCanonicalPath(String path) {
		try {
			return new File(path).getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
			return path;
		}
	}
	
	public static String exec(String program) throws IOException, InterruptedException{
	    return exec(program, null);
	}
	
	public static String exec(String program, String[] arguments) throws IOException, InterruptedException{
	    ProcessRunner runner = new ProcessRunner(program, arguments);
	    runner.waitFor();
	    return runner.formattedResult();  
	}

	public static ProcessRunner start(String program, String[] arguments) throws IOException {
	    return new ProcessRunner(program, arguments);
	}

	public static String execAndDestroy(String program, String[] arguments, String expectedOutput, long timeout) throws IOException{
        ProcessRunner runner = new ProcessRunner(program, arguments);
        runner.destroy(expectedOutput, timeout);
        return runner.formattedResult();
    }
	
	public static class DestroyTimeoutException extends RuntimeException{
	}
	
	public static class ProcessTerminatedBeforeDestroyException extends RuntimeException{
	}
	
	public static class ProcessRunner{
	    
	    final long _startTime;
	    
	    private final String _command;
	    
        private final StreamReader _inputReader;
        
        private final StreamReader _errorReader;
        
        private final Process _process;
        
        private int _result;
	    
	    public ProcessRunner(String program, String[] arguments) throws IOException{
	        _command = generateCommand(program, arguments);
            _process = Runtime.getRuntime().exec(_command);
            _inputReader = new StreamReader(_process.getInputStream());
            _errorReader = new StreamReader(_process.getErrorStream());
            _startTime = System.currentTimeMillis();
	    }
	    
	    private String generateCommand (String program, String[] arguments){
            String command = program;
            if(arguments != null){
                for (int i = 0; i < arguments.length; i++) {
                    command += " " + arguments[i];
                }
            }
            return command;
	    }
	    
	    public int waitFor() throws InterruptedException{
	        _result = _process.waitFor();
	        stopReaders();
	        return _result;
	    }
	    
	    private boolean outputHasStarted(){
	        return _inputReader.outputHasStarted() || _errorReader.outputHasStarted();
	    }

	    private boolean outputContains(String str){
	        return _inputReader.outputContains(str) || _errorReader.outputContains(str);
	    }
	    
	    private void checkTimeOut(long time){
	    	try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	        if(System.currentTimeMillis() - _startTime > time){
	            throw new DestroyTimeoutException();
	        }
	    }
	    
	    public void destroy(String expectedOutput, long timeout){
	        try{
    	        checkIfStarted(expectedOutput, timeout);
	        } 
	        finally {
	        	destroy();
	        }
	    }

	    public void destroy(){
	        try{
    	        checkIfTerminated();
    	        
    	        // Race condition: If the process is terminated right here , it may
    	        // terminate successfully before being destroyed.
    	        
	        } finally {
	            _process.destroy();
	            stopReaders();
	        }
	    }

	    public void write(String msg) throws IOException {
	    	OutputStreamWriter out = new OutputStreamWriter(_process.getOutputStream());
			out.write(msg + "\n");
			out.flush();
	    }
	    
        public void checkIfStarted(String expectedOutput, long timeout) {
            while(! outputHasStarted()){
	            checkTimeOut(timeout);
	        }
	        while(! outputContains(expectedOutput)){
	            checkTimeOut(timeout);
	        }
        }

        private void checkIfTerminated() {
            boolean ok = false;
	        try{
	            _process.exitValue();
	        }catch (IllegalThreadStateException ex){
	            ok = true;
	        }
	        if(! ok){
	            throw new ProcessTerminatedBeforeDestroyException();
	        }
        }
	    
	    private void stopReaders(){
	        _inputReader.stop();
	        _errorReader.stop();
	    }
	    
	    public String formattedResult(){
	        String res = formatOutput("IOServices.exec", _command); 
	        
	        if(_inputReader.hasResult()){
	            res += formatOutput("out", _inputReader.result()); 
	        }
	        if(_errorReader.hasResult()){
	            res += formatOutput("err", _errorReader.result()); 
	        }
	        
	        res += formatOutput("result", new Integer(_result).toString());
	        
	        return res;  

	    }
	    
	    private String formatOutput(String task, String output){
	        return headLine(task) + output + "\n";
	    }
	    
	    private String headLine(String task){
	        return "\n" + task + "\n----------------\n";  
	    }
	    
	}
	

    static class StreamReader implements Runnable {
        
        private final Object _lock = new Object();
        
        private final InputStream _stream;
        
        private final Thread _thread;
        
        private final StringBuffer _stringBuffer = new StringBuffer();
        
        private boolean _stopped;
        
        private String _result;
        
        StreamReader(InputStream stream){
            _stream = stream;
            _thread = new Thread(this);
            _thread.start();
        }
        
        public void run() {
            final InputStream bufferedStream = new BufferedInputStream(_stream);
            try {
                while(! _stopped){
                    int i = bufferedStream.read();
                    if(i >= 0){
                        synchronized(_lock){
                            _stringBuffer.append((char)i);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            _result = _stringBuffer.toString();
        }
        
        public boolean outputHasStarted(){
            synchronized(_lock){
                return _stringBuffer.length() > 0;
            }
        }
        
        public boolean outputContains(String str){
            synchronized(_lock){
                return _stringBuffer.toString().indexOf(str) >= 0;
            }
        }
        
        public void stop(){
            _stopped = true;
            try {
                _thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        public boolean hasResult(){
            return _result != null && _result.length() > 0;
        }
        
        public String result(){
            return _result;
        }
    }
    
    public static String joinArgs(String separator, String[] args, boolean doQuote)
    {
        StringBuffer buffer = new StringBuffer();
        for (String arg : args)
        {
            if (buffer.length() > 0) buffer.append(separator);
            buffer.append((doQuote ? quote(arg) : arg));
        }
        return buffer.toString();
    }
    
    public static String quote(String s)
    {
        if (s.startsWith("\"")) return s;
        return "\"" + s + "\"";
    }

}
