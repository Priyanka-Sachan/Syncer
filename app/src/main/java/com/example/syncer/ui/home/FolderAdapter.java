package com.example.syncer.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.syncer.R;
import com.example.syncer.database.Folder;

class FolderAdapter extends ListAdapter<Folder, FolderAdapter.FolderViewHolder> {
    OnItemClickListener listener;

    public FolderAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Folder> DIFF_CALLBACK = new DiffUtil.ItemCallback<Folder>() {
        @Override
        public boolean areItemsTheSame(Folder oldItem, Folder newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(Folder oldItem, Folder newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getPath().equals(newItem.getPath()) &&
                    oldItem.getInSync() == newItem.getInSync() &&
                    oldItem.getHour() == newItem.getHour() &&
                    oldItem.getMinute() == newItem.getMinute();
        }
    };

    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_folder, parent, false);
        return new FolderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {
        Folder current = getItem(position);
        holder.title.setText(current.getTitle());
        holder.path.setText(current.getPath());
    }

    public Folder getFolderAt(int position) {
        return getItem(position);
    }

    public class FolderViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView path;

        public FolderViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.folder_name);
            path = itemView.findViewById(R.id.folder_last_synced);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Folder folder);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
