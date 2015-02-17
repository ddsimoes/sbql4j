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

package org.polepos.teams.jpa;
import org.polepos.circuits.sepang.SepangDriver;
import org.polepos.teams.jpa.data.JpaTree;
import org.polepos.teams.jpa.data.JpaTreeVisitor;


/**
 * @author Christian Ernst
 */
public class SepangJpa extends JpaDriver implements SepangDriver {
    
    private Object oid;
    
	public void write(){
		begin();
        JpaTree tree = JpaTree.createTree(setup().getDepth());
        db().persist(tree);
//        JpaTree.traverse(tree, new JpaTreeVisitor() {
//            public void visit(JpaTree tree) {
//               	db().persist(tree);
//            }
//        });
        oid = tree.getOid();
		commit();
	}

	public void read(){
		begin();
        JpaTree tree = (JpaTree)db().find(JpaTree.class,oid);
        JpaTree.traverse(tree, new JpaTreeVisitor() {
            public void visit(JpaTree tree) {
                addToCheckSum(tree.getDepth());
            }
        });
        commit();
	}
    
	public void delete(){
		begin();
        JpaTree tree = (JpaTree)db().find(JpaTree.class,oid);
        db().remove(tree);
//        JpaTree.traverse(tree, new JpaTreeVisitor() {
//            public void visit(JpaTree tree) {
//                db().remove(tree);
//            }
//        });
        
		commit();
	}
    

}
