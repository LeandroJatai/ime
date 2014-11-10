/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifg.ime.dao;


import java.util.Collection;
import java.util.List;

/**
 *r
 * @author Leandro
 */
public abstract class DAO<T> {


	//abstract public boolean insert(T arg0);
    //abstract public boolean upDate(T arg0);
   // abstract public boolean delete(T arg0);
    abstract public List<T> selectAll();
    

/*    public boolean insert(T arg0){
      return false;  
    }
    public boolean upDate(T arg0){
        return false;
    }
    public boolean delete(T arg0){
        return false;
    }

    public SortedSet<T> selectAll(){
        return null;
    }
    
*/
}
