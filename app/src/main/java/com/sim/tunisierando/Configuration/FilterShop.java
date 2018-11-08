package com.sim.tunisierando.Configuration;

import android.widget.Filter;

import com.sim.tunisierando.Entities.Product;
import com.sim.tunisierando.adapters.ProduitListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wassim on 11/12/2017.
 */

public class FilterShop extends Filter {
    private  List<Product> filterList;
    private  ProduitListAdapter adapter;

    public FilterShop(List<Product> filterList, ProduitListAdapter adapter) {
        this.adapter=adapter;
        this.filterList=filterList;
    }



    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();
        //CHECK CONSTRAINT VALIDITY
        if(constraint != null && constraint.length() > 0)
        {
            //CHANGE TO UPPER
            constraint=constraint.toString().toUpperCase();
            //STORE OUR FILTERED PLAYERS
            ArrayList<Product> filteredProd=new ArrayList<>();
            for (int i=0;i<filterList.size();i++)
            {
                //CHECK
                if(filterList.get(i).getType().toUpperCase().contains(constraint) || filterList.get(i).getTitle().toUpperCase().contains(constraint) || (String.valueOf(filterList.get(i).getPrix())).toUpperCase().contains(constraint))
                {
                    //ADD PLAYER TO FILTERED PLAYERS
                    filteredProd.add(filterList.get(i));
                }
            }
            results.count=filteredProd.size();
            results.values=filteredProd;
        }else
        {
            results.count=filterList.size();
            results.values=filterList;
        }
        return results;
    }
    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.setList((ArrayList<Product>) results.values);
        //REFRESH
        adapter.notifyDataSetChanged();
    }
}
