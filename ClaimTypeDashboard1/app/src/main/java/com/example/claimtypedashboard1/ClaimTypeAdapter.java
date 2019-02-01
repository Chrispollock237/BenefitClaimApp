package com.example.claimtypedashboard1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import android.support.v7.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class ClaimTypeAdapter extends RecyclerView.Adapter<ClaimTypeAdapter.ViewHolder>{

    /***
     * The adapter class for the RecyclerView, contains the Claim data.
     */
        // Member variables.
        private ArrayList<ClaimType> mTypeData;
        private Context mContext;
        Client currentClient;

        /**
         * Constructor that passes in the claim type data and the context.
         *
         * @param claimData ArrayList containing the claim data.
         * @param context Context of the application.
         */
        ClaimTypeAdapter(Context context, ArrayList<ClaimType> claimData) {
            this.mTypeData = claimData;
            this.mContext = context;
        }

        /**
         * Required method for creating the viewholder objects.
         *
         * @param parent The ViewGroup into which the new View will be added
         *               after it is bound to an adapter position.
         * @param viewType The view type of the new View.
         * @return The newly created ViewHolder.
         */
        @Override
        public ClaimTypeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new ViewHolder(LayoutInflater.from(mContext).inflate
                    (R.layout.list_item, parent, false));
        }

        /**
         * Required method that binds the data to the viewholder.
         *
         * @param holder The viewholder into which the data should be put.
         * @param position The adapter position.
         */
        @Override
        public void onBindViewHolder(ClaimTypeAdapter.ViewHolder holder, int position) {

            // Get current claim type.
            ClaimType currentType = mTypeData.get(position);

            // Populate the textviews with data.
            holder.bindTo(currentType);
        }

        /**
         * Required method for determining the size of the data set.
         *
         * @return Size of the data set.
         */
        @Override
        public int getItemCount() {
            return mTypeData.size();
        }

        /**
         * ViewHolder class that represents each row of data in the RecyclerView.
         */
        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            // Member Variables for the TextViews
            private TextView mTypeText;
            private TextView mInfoText;
            private ImageView mClaimImage;


            /**
             * Constructor for the ViewHolder, used in onCreateViewHolder().
             *
             * @param itemView The rootview of the list_item.xml layout file.
             */
            ViewHolder(View itemView) {
                super(itemView);

                // Initialize the views.
                mTypeText = itemView.findViewById(R.id.claimtype);
                mInfoText = itemView.findViewById(R.id.subTitle);
                mClaimImage = itemView.findViewById(R.id.claimImage);

                // Set the OnClickListener to the entire view.
                itemView.setOnClickListener(this);
            }

            void bindTo(ClaimType currentType){

                // Populate the textviews with data.
                mTypeText.setText(currentType.getType());
                mInfoText.setText(currentType.getInfo());
                Glide.with(mContext).load(currentType.getImageResource()).into(mClaimImage);
            }

            @Override
            public void onClick(View view) {

                // Get the currentClient object to be passed as an extra in the Intent
                // for the FormActivity
                currentClient = MainActivity.getClient();

                // Get the ClaimType object for the item that was clicked using getAdapterPosition():
                ClaimType currentClaim = mTypeData.get(getAdapterPosition());

                // Launch the FormActivity, put the title and image_resource as extras in the
                // Intent, and call the startActivity() method for the current context, passing the
                // new Intent.
                Intent formIntent = new Intent(mContext, FormActivity.class);
                formIntent.putExtra("title", currentClaim.getType());
                formIntent.putExtra("image_resource", currentClaim.getImageResource());
                formIntent.putExtra("Client", currentClient);
                mContext.startActivity(formIntent);
            }
        }
}
