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

package org.polepos.teams.jpa.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Christian Ernst
 */
@Entity
public class JPN1 {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long oid;
    
    public String s0;
    
    public String s1;
    
    public String s2;
    
    public String s3;
    
    public String s4;
    
    public String s5;
    
    public String s6;
    
    public String s7;
    
    public String s8;
    
    public String s9;

    
    public JPN1(){
        
    }
    
    
    public static JPN1 generate(int index){
        JPN1 n1 = new JPN1();
        n1.setStrings(index);
        return n1;
    }
    
    private void setStrings(int index){
        String str = "N" + index + "aabbccddeeffgghhjjKK";
        str = str.substring(0,20);
        s0 = str;
        s1 = str;
        s2 = str;
        s3 = str;
        s4 = str;
        s5 = str;
        s6 = str;
        s7 = str;
        s8 = str;
        s9 = str;
    }

    
    public String getS0() {
        return s0;
    }

    
    public void setS0(String s0) {
        this.s0 = s0;
    }

    
    public String getS1() {
        return s1;
    }

    
    public void setS1(String s1) {
        this.s1 = s1;
    }

    
    public String getS2() {
        return s2;
    }

    
    public void setS2(String s2) {
        this.s2 = s2;
    }

    
    public String getS3() {
        return s3;
    }

    
    public void setS3(String s3) {
        this.s3 = s3;
    }

    
    public String getS4() {
        return s4;
    }

    
    public void setS4(String s4) {
        this.s4 = s4;
    }

    
    public String getS5() {
        return s5;
    }

    
    public void setS5(String s5) {
        this.s5 = s5;
    }

    
    public String getS6() {
        return s6;
    }

    
    public void setS6(String s6) {
        this.s6 = s6;
    }

    
    public String getS7() {
        return s7;
    }

    
    public void setS7(String s7) {
        this.s7 = s7;
    }

    
    public String getS8() {
        return s8;
    }

    
    public void setS8(String s8) {
        this.s8 = s8;
    }

    
    public String getS9() {
        return s9;
    }

    
    public void setS9(String s9) {
        this.s9 = s9;
    }

    
}
