package com.example.tmfappointment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> implements Filterable {
    private static final String LOG_TAG = AppointmentAdapter.class.getName();

    private ArrayList<Appointment> mAppointmentsData;
    private ArrayList<Appointment> mAppointmentsDataAll;
    private Context mContext;
    private int lastPosition = -1;

    public AppointmentAdapter(Context context, ArrayList<Appointment> appointmentsData) {
        this.mAppointmentsData = appointmentsData;
        this.mAppointmentsDataAll = appointmentsData;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.appointment_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(AppointmentAdapter.ViewHolder holder, int position) {
        // aktualis pozicioju elem letrehozasa
        Appointment currentAppointment = mAppointmentsData.get(position);

        holder.bindTo(currentAppointment);

        // beuszasi animacio hozzaadasa
        if (holder.getAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return mAppointmentsData.size();
    }

    @Override
    public Filter getFilter() { return appointmentFilter; }

    private Filter appointmentFilter = new Filter() {
        // kereses vegrehajtasa
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Appointment> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if (charSequence == null || charSequence.length() == 0) {
                results.count = mAppointmentsDataAll.size();
                results.values = mAppointmentsDataAll;
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (Appointment appointment : mAppointmentsDataAll) {
                    Log.d(LOG_TAG, filterPattern);
                    Log.d(LOG_TAG, appointment.getExternalId().toLowerCase());
                    if (appointment.getExternalId().toLowerCase().contains(filterPattern)) {
                        filteredList.add(appointment);
                    }
                }

                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }

        // kereses eredmenyenek megjelenitese
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mAppointmentsData = (ArrayList) results.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView bannerImageView;
        private TextView titleTextView;
        private TextView descriptionTextView;
        private TextView noteTextView;
        private TextView placeTextView;
        private TextView validForTextView;
        private TextView contactPersonTextView;
        private TextView contactEntityTextView;
        private TextView contactMediumTextView;
        private TextView categoryTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // az adattagokhoz hozzarendeljuk a kapott layout megfelelo elemeit
            bannerImageView = itemView.findViewById(R.id.imageView_banner);
            titleTextView = itemView.findViewById(R.id.textView_title);
            descriptionTextView = itemView.findViewById(R.id.textView_description);
            noteTextView = itemView.findViewById(R.id.textView_note);
            placeTextView = itemView.findViewById(R.id.textView_place);
            validForTextView = itemView.findViewById(R.id.textView_validFor);
            contactPersonTextView = itemView.findViewById(R.id.textView_contactPerson);
            contactEntityTextView = itemView.findViewById(R.id.textView_contactEntity);
            contactMediumTextView = itemView.findViewById(R.id.textView_contactMedium);
            categoryTextView = itemView.findViewById(R.id.textView_category);

            // a gombokhoz hozzarendeljuk a megfelelo funkciokat
            itemView.findViewById(R.id.button_done).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(LOG_TAG, "Done button clicked!");
                    ((AppointmentListActivity)mContext).deleteAppointment(titleTextView.getText().toString());
                }
            });
            itemView.findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(LOG_TAG, "Cancel button clicked!");
                    ((AppointmentListActivity)mContext).deleteAppointment(titleTextView.getText().toString());
                }
            });
            itemView.findViewById(R.id.button_edit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(LOG_TAG, "Edit button clicked!");
                    ((AppointmentListActivity)mContext).openHomePage();
                }
            });
        }

        // a layout elemeihez hozzarendeljuk az aktualis appointment megfelelo adattagjait
        public void bindTo(Appointment currentAppointment) {
            titleTextView.setText(currentAppointment.getExternalId());
            descriptionTextView.setText(currentAppointment.getDescription());
            noteTextView.setText(currentAppointment.getNote());
            placeTextView.setText(currentAppointment.getRelatedPlace());
            validForTextView.setText(currentAppointment.getValidFor().toString());
            contactPersonTextView.setText(currentAppointment.getRelatedParty());
            contactEntityTextView.setText(currentAppointment.getRelatedEntity());
            contactMediumTextView.setText(currentAppointment.getContactMedium());
            categoryTextView.setText(currentAppointment.getCategory().toString());

            Glide.with(mContext).load(currentAppointment.getAttachment()).into(bannerImageView);
        }
    }
}

