/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.boomrich.freshmeat.util;

/**
 *
 * @author will
 */
public class Page {
    private int start;
    private int count;
    private int total;
    private String param;
    
    public Page(int start,int count)
    {
        this.start = start;
        this.count = count;
    }
    
    public int getStart()
    {
        return start;
    }
    
    public void setStart(int start)
    {
        this.start = start;
    }
    
    public int getCount()
    {
        return count;
    }
    
    public void setCount(int count)
    {
        this.count = count;
    }
    
    public int getTotal()
    {
        return total;
    }
    
    public void setTotal()
    {
        this.total = total;
    }
    
    public String getParam()
    {
        return param;
    }
    
    public void setParam(String param)
    {
        this.param = param;
    }
    
    public boolean isHasPrevious()
    {
        if(start==0)
            return false;
        return true;
    }
    
    public boolean isHasNext()
    {
        if(start==getLast())
            return false;
        return true;
    }
    
    public int getTotalPage()
    {
        int totalPage;
        
        if(total%count == 0)
            totalPage = total / count;
        else
            totalPage = total / count + 1;
        
        if(totalPage == 0)
            totalPage = 1;
        return totalPage;
    }
    
    public int getLast()
    {
        int last;
        
        if(total % count == 0 )
            last = total - count;
        else
            last = total - total % count;
        
        last = last < 0 ? 0 : last;
        
        return last;
    }
}
