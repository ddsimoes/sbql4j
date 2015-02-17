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

package org.polepos.teams.jvi;
import org.polepos.circuits.sepang.SepangDriver;
import org.polepos.teams.jvi.data.JviTree;
import org.polepos.teams.jvi.data.JviTreeVisitor;


/**
 * @author Christian Ernst
 */
public class SepangJvi extends JviDriver implements SepangDriver {
    
    private Object oid;
    
	public void write(){
		begin();
        JviTree tree = JviTree.createTree(setup().getDepth());
        db().makePersistent(tree);
        oid = db().getOidAsLong(tree);
		commit();
	}

	public void read(){
		begin();
        JviTree tree = (JviTree)(Object)db().loidToJod((Long)oid);
        JviTree.traverse(tree, new JviTreeVisitor() {
            public void visit(JviTree tree) {
                addToCheckSum(tree.getDepth());
            }
        });
        commit();
	}
    
	public void delete(){
		begin();
		JviTree tree = (JviTree)(Object)db().loidToJod((Long)oid);
        JviTree.traverse(tree, new JviTreeVisitor() {
            public void visit(JviTree tree) {
                db().deleteObject(tree);
            }
        });
		commit();
	}
    

}
