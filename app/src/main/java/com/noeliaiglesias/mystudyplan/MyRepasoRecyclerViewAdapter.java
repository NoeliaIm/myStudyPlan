package com.noeliaiglesias.mystudyplan;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.noeliaiglesias.mystudyplan.databinding.FragmentRepasoBinding;
import com.noeliaiglesias.mystudyplan.placeholder.Repaso;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link com.noeliaiglesias.mystudyplan.placeholder.Repaso}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyRepasoRecyclerViewAdapter extends RecyclerView.Adapter<MyRepasoRecyclerViewAdapter.ViewHolder> {

    private final List<Repaso> mValues;

    public MyRepasoRecyclerViewAdapter(List<Repaso> items) {
        mValues = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentRepasoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mAsignaturaView.setText(mValues.get(position).getAsignatura());
        holder.mTemaView.setText(mValues.get(position).getTema());
        if(mValues.get(position).getFechaSiguienteRepaso()!= null){
            holder.mFechaView.setText(mValues.get(position).getFechaSiguienteRepaso().format(DateTimeFormatter.ofPattern("EEE, dd-MM-yyyy")));
            if(mValues.get(position).getFechaSiguienteRepaso().equals(LocalDate.now())){
                holder.mFechaView.setBackgroundResource(R.color.morado);
            }else{
                holder.mFechaView.setBackgroundResource(R.color.white);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mAsignaturaView;
        public final TextView mTemaView;
        public final TextView mFechaView;
        public Repaso mItem;

        public ViewHolder(FragmentRepasoBinding binding) {
            super(binding.getRoot());
            mAsignaturaView = binding.itemRepasoAsignatura;
            mTemaView = binding.itemRepasoTema;
            mFechaView = binding.itemRepasoFechaSig;
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mAsignaturaView.getText() + "'";
        }
    }
}