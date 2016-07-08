/*
 * Copyright (C) 2016 jcgarner
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.pikatimer.util.fileTransports;

import com.pikatimer.results.OutputPortal;
import com.pikatimer.util.FileTransport;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author jcgarner
 */
public class LocalTransport implements FileTransport {
    Boolean goodToGo = false;
    String basePath;
    OutputPortal parent;
    

    @Override
    public boolean isOK() {
        return goodToGo;
    }

    @Override
    public void save(String filename, String contents) {
        System.out.println("LocalTransport.save called for " + filename);
        if (goodToGo && ! basePath.isEmpty()) {
            
            try {
                FileUtils.writeStringToFile(new File(FilenameUtils.concat(basePath, filename)), contents);
            } catch (IOException ex) {
                Logger.getLogger(LocalTransport.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void setOutputPortal(OutputPortal op) {
        parent = op; 
        refreshConfig();
    }

    @Override
    public void refreshConfig() {
        if(parent != null && parent.getBasePath() != null && ! parent.getBasePath().isEmpty()) {
            basePath = FilenameUtils.normalizeNoEndSeparator(parent.getBasePath());
            
            File baseDir = new File(basePath);
            

            // does it exist?
            if (!baseDir.exists()) {
              baseDir.mkdir();
            }
            
            // it should now... 
            if (baseDir.exists()) {
                goodToGo = true;
            } else {
                goodToGo = false;
            }
            
        } else {
            basePath = null; 
        }
        
    }

    
    
}
