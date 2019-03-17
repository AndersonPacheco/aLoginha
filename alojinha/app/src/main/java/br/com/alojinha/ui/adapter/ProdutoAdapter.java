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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.alojinha.R;
import br.com.alojinha.model.Produto;
import br.com.alojinha.util.Utils;

public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.ProdutoHolder> {

    private Context ctx;
    private List<Produto> data = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onClickProduto(Produto item);
    }

    public ProdutoAdapter(Context context, OnItemClickListener listener) {
        this.listener = listener;
        this.ctx = context;
    }

    public void setData(List<Produto> produtos) {
        this.data = produtos;
    }

    @NonNull
    @Override
    public ProdutoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(ctx)
                .inflate(R.layout.card_view_produto, viewGroup, false);

        ProdutoHolder holder = new ProdutoHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoHolder produtoHolder, int i) {
        ProdutoHolder holder = produtoHolder;

        holder.tv_descricao.setText(data.get(i).getNome());
        NumberFormat defaultFormat = NumberFormat.getInstance(Utils.getCurrentLocale(ctx));
        holder.tv_preco_de.setText("De: " + defaultFormat.format(data.get(i).getPrecoDe()));
        holder.tv_preco_por.setText("Por " + defaultFormat.format(data.get(i).getPrecoPor()));
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

    public class ProdutoHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tv_descricao;
        private TextView tv_preco_de;
        private TextView tv_preco_por;
        private ImageView iv_produto;
        private ImageView iv_indicator;

        public ProdutoHolder(@NonNull View itemView) {
            super(itemView);
            tv_descricao = itemView.findViewById(R.id.tv_descricao);
            tv_preco_de = itemView.findViewById(R.id.tv_preco_de);
            tv_preco_por = itemView.findViewById(R.id.tv_preco_por);
            iv_produto = itemView.findViewById(R.id.iv_produto);
            iv_indicator = itemView.findViewById(R.id.iv_indicator);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClickProduto(data.get(getAdapterPosition()));
        }
    }

    public Produto getItemByPosition(int position) {
        return data.get(position);
    }

}
