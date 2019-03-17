package br.com.alojinha.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.List;
import br.com.alojinha.R;
import br.com.alojinha.model.Categoria;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.CategoriaHolder>{

    private List<Categoria> data;
    private Context ctx;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onClickCategoria(Categoria item);
    }

    public CategoriaAdapter(Context context, OnItemClickListener listener, List<Categoria> categorias) {
        this.data = categorias;
        this.listener = listener;
        this.ctx = context;
    }

    @NonNull
    @Override
    public CategoriaHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(ctx)
                .inflate(R.layout.card_view_categoria, viewGroup, false);

        CategoriaHolder holder = new CategoriaHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaHolder categoriaHolder, int i) {
        CategoriaHolder holder = categoriaHolder;

        holder.tv_descricao.setText(data.get(i).getDescricao());
        Glide
                .with(ctx)
                .load(data.get(i).getUrlImagem())
                .centerCrop()
                .placeholder(R.drawable.loading)
                .error(R.mipmap.ic_nao_disponivel)
                .into(holder.iv_produto);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class CategoriaHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tv_descricao;
        private ImageView iv_produto;

        public CategoriaHolder(@NonNull View itemView) {
            super(itemView);
            tv_descricao = itemView.findViewById(R.id.tv_categoria);
            iv_produto = itemView.findViewById(R.id.iv_produto);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClickCategoria(data.get(getAdapterPosition()));
        }
    }

}
