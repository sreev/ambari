/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.ambari.client;

import java.net.URI;
import java.net.URL;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.apache.ambari.common.rest.entities.StackInformation;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class StackHistory extends Command {

    String[] args = null;
    Options options = null;
    
    String urlPath = "/stacks";
    URL resourceURL = null;
    CommandLine line;
    
    public StackHistory() {
    }
    
    public StackHistory (String [] args) throws Exception {  
        /*
         * Build options for stack history
         */
        this.args = args;
        addOptions();
        this.resourceURL = new URL (""+this.baseURLString+this.urlPath);
    }
    
    public void printUsage () {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp( "ambari stack history", this.options);
    }
    
    public void addOptions () {
             
        Option help = new Option( "help", "Help" );
        Option tree = new Option( "tree", "tree representation" );
        
        OptionBuilder.withArgName("stack_name");
        OptionBuilder.isRequired();
        OptionBuilder.hasArg();
        OptionBuilder.withDescription( "Name of the stack");
        Option name = OptionBuilder.create( "name" );
        
        this.options = new Options();  
        options.addOption( name );
        options.addOption( tree );
        options.addOption(help);
    }
    
    public void parseCommandLine() {
     
        // create the parser
        CommandLineParser parser = new GnuParser();
        try {
            // parse the command line arguments
            line = parser.parse(this.options, this.args );
            
            if (line.hasOption("help")) {
                printUsage();
                System.exit(0);
            }
        }
        catch( ParseException exp ) {
            // oops, something went wrong
            System.err.println( "Command parsing failed. Reason: <" + exp.getMessage()+">\n" );
            printUsage();
            System.exit(-1);
        } 
    }
    
    private static URI getBaseURI() {
        return UriBuilder.fromUri(
                "http://localhost:4080/rest/").build();
    }
    
    
    public void run() throws Exception {
        /* 
         * Parse the command line to get the command line arguments
         */
        parseCommandLine();
        
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        WebResource service = client.resource(getBaseURI());
        String stackName = line.getOptionValue("name");
        boolean tree = line.hasOption("tree");
          
        /*
         * Get stack revisions
         */
        ClientResponse response = service.path("stacks/"+stackName+"/revisions").accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        if (response.getStatus() == 404) { 
            System.out.println("Stack ["+stackName+"] does not exist");
            System.exit(-1);
        }
        
        if (response.getStatus() == 204) {
            System.out.println("No revisions available for Stack ["+stackName+"]");
            System.exit(0);
        }
        
        if (response.getStatus() != 200) { 
            System.err.println("Stack history command failed. Reason [Code: <"+response.getStatus()+">, Message: <"+response.getHeaders().getFirst("ErrorMessage")+">]");
            System.exit(-1);
        }
        /* 
         * Retrieve the stack Information list from the response
         */
        List<StackInformation> bpInfos = response.getEntity(new GenericType<List<StackInformation>>(){});
        for (StackInformation bpInfo : bpInfos) {
            printStackInformation (service, bpInfo, tree);
        }
    }
}