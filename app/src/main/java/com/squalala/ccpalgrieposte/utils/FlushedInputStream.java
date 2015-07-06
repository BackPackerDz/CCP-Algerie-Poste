package com.squalala.ccpalgrieposte.utils;

 import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
 
public class FlushedInputStream extends FilterInputStream {
   public FlushedInputStream(final InputStream inputStream) {
     super(inputStream);
   }
 
   /**
    * Overriding the skip method to actually skip n bytes.
    * This implementation makes sure that we actually skip
    * the n bytes no matter what.
    */
   @Override
   public long skip(final long n) throws IOException {
     long totalBytesSkipped = 0L;
 
     while (totalBytesSkipped < n) {
       long bytesSkipped = in.skip(n - totalBytesSkipped);
 
       if (bytesSkipped == 0L) {
         int bytesRead = read();
 
         if (bytesRead < 0) { // we reached EOF
           break;
         }
 
         bytesSkipped = 1;
       }
 
       totalBytesSkipped += bytesSkipped;
     }
 
     return totalBytesSkipped;
   }
 } 