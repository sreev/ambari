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
package org.apache.ambari.common.rest.entities;

import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Entity for defining a list of repositories inside a Stack.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PackageType", propOrder = {
    "urls"
})
@XmlRootElement
public class RepositoryKind {

    @XmlAttribute(required = true)
    private String kind;
    
    @XmlElement(required = true)
    private List<String> urls;

    public RepositoryKind() {
      // PASS
    }

    public RepositoryKind(String name, String... urls) {
      this.kind = name;
      this.urls = Arrays.asList(urls);
    }

    @Override
    public boolean equals(Object other) {
      if (this == other) {
        return true;
      } else if (other == null || other.getClass() != getClass()) {
        return false;
      } else {
        RepositoryKind repoKind = (RepositoryKind) other;
        if (!kind.equals(repoKind.kind) || urls.size() != repoKind.urls.size()){
          return false;
        } else {
          for(int i = 0; i < urls.size(); i++) {
            if (!urls.get(i).equals(repoKind.urls.get(i))) {
              return false;
            }
          }
          return true;
        }
      }
    }

    @Override
    public int hashCode() {
      return kind.hashCode();
    }

    /**
     * Gets the value of the urls property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public List<String> getUrls() {
        return urls;
    }

    /**
     * Sets the value of the locationURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrls(List<String> value) {
        this.urls = value;
    }

    /**
     * Gets the value of the kind property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKind() {
        return kind;
    }

    /**
     * Sets the value of the kind property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKind(String value) {
        this.kind = value;
    }

}
