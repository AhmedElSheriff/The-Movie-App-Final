package com.example.android.themovieapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Abshafi on 7/27/2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<MoviesData> array;
    private LayoutInflater inflater;
    private Clicklistener clicklistener;




    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {

        holder.itemView.clearAnimation();
    }






    RecyclerViewAdapter(Context context,ArrayList<MoviesData> array)
    {
        this.context = context;
        this.array = array;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private void setAnimation(RecyclerView.ViewHolder viewHolder)
    {

            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewHolder.itemView.setAnimation(animation);


    }




    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;


        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
             image = (ImageView) itemView.findViewById(R.id.oneMovieBlockImageView);


        }


        @Override
        public void onClick(View v) {

            if(clicklistener != null)
            {
                clicklistener.itemClicked(v,getPosition());

            }

        }


    }

    public void setClickListener(Clicklistener clickListener)
    {
        this.clicklistener = clickListener;
    }

    public interface Clicklistener{

        void itemClicked(View view, int position);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootview = inflater.inflate(R.layout.movie_block,parent,false);

        ViewHolder viewHolder = new ViewHolder(rootview);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        setAnimation(holder);
        Picasso.with(context).load(array.get(position).getPoster_path()).into(holder.image);

    }



    @Override
    public int getItemCount() {
        return array.size();
    }


}
