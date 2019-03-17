package br.com.alojinha.ui.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import br.com.alojinha.R;
import br.com.alojinha.data.BannerDelegate;
import br.com.alojinha.data.BannerRes;
import br.com.alojinha.data.BannerTask;
import br.com.alojinha.data.CategoriaDelegate;
import br.com.alojinha.data.CategoriaRes;
import br.com.alojinha.data.CategoriaTask;
import br.com.alojinha.data.ProdutoDelegate;
import br.com.alojinha.data.ProdutoRes;
import br.com.alojinha.data.ProdutoTask;
import br.com.alojinha.databinding.FragmentHomeBinding;
import br.com.alojinha.model.Banner;
import br.com.alojinha.model.Categoria;
import br.com.alojinha.model.Produto;
import br.com.alojinha.ui.activity.DetalheProdutoActivity;
import br.com.alojinha.ui.activity.ProdutoPorCategoriaActivity;
import br.com.alojinha.ui.adapter.BannerAdapter;
import br.com.alojinha.ui.adapter.CategoriaAdapter;
import br.com.alojinha.ui.adapter.ProdutoAdapter;
import br.com.alojinha.util.Alert;
import br.com.alojinha.util.Constantes;
import br.com.alojinha.databinding.BannerViewBinding;


public class HomeFragment extends Fragment implements BannerDelegate, CategoriaDelegate,
        ProdutoDelegate, ProdutoAdapter.OnItemClickListener, CategoriaAdapter.OnItemClickListener {

    private FragmentHomeBinding binding;
    private ProdutoAdapter produtoAdapter;
    private CategoriaAdapter categoriaAdapter;
    private BannerViewBinding bannerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        doBanner();
        doCategoria();
        doProduto();

        return binding.getRoot();
    }

    public void doBanner(){
        new BannerTask(this, getContext()).execute("banner");
    }


    @Override
    public void onBannerResult(BannerRes bannerRes) {
        if(bannerRes != null) {
            if (bannerRes.getStatusCode() == HttpURLConnection.HTTP_OK) {
                binding.pager.setAdapter(new BannerAdapter(getActivity(), bannerRes.getBanner()));
                binding.viewPagerIndicator.setupWithViewPager(binding.pager);
                binding.viewPagerIndicator.addOnPageChangeListener(listener);
            } else {
                Alert.showSimpleDialog(bannerRes.getMensagem(), getActivity(), null);
            }
        } else {
            //Alert.showSimpleDialog(getResources().getString(R.string.msg_problema_login), this, null);
        }
    }



    private ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void doCategoria(){
        new CategoriaTask(this, getContext()).execute("categoria");
    }

    @Override
    public void onCategoriaResult(CategoriaRes categoriaRes) {
        if(categoriaRes != null) {
            if (categoriaRes.getStatusCode() == HttpURLConnection.HTTP_OK) {
                binding.recyclerCategorias.setHasFixedSize(true);
                binding.recyclerCategorias.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                categoriaAdapter = new CategoriaAdapter(getActivity(), this, categoriaRes.getCategoriaList());
                binding.recyclerCategorias.setAdapter(categoriaAdapter);
            } else {
                Alert.showSimpleDialog(categoriaRes.getMensagem(), getActivity(), null);
            }
        } else {
            //Alert.showSimpleDialog(getResources().getString(R.string.msg_problema_login), this, null);
        }
    }

    public void doProduto(){
        new ProdutoTask(this, getActivity()).execute("produto/maisvendidos", Constantes.REQUEST_METHOD_GET);
    }

    @Override
    public void onProdutoResult(ProdutoRes produtoRes) {
        if(produtoRes != null) {
            if (produtoRes.getStatusCode() == HttpURLConnection.HTTP_OK) {
                binding.recyclerMaisVendidos.setHasFixedSize(true);
                binding.recyclerMaisVendidos.setLayoutManager(new LinearLayoutManager(getActivity()));
                produtoAdapter = new ProdutoAdapter(getActivity(), this);
                produtoAdapter.setData(produtoRes.getProdutoList());
                binding.recyclerMaisVendidos.setAdapter(produtoAdapter);
            } else {
                Alert.showSimpleDialog(produtoRes.getMensagem(), getActivity(), null);
            }
        } else {
            //Alert.showSimpleDialog(getResources().getString(R.string.msg_problema_login), this, null);
        }
    }

    @Override
    public void onClickProduto(Produto item) {
        Intent intent = new Intent(getActivity(), DetalheProdutoActivity.class);
        intent.putExtra(getString(R.string.param_produto), item);
        startActivity(intent);
    }

    @Override
    public void onClickCategoria(Categoria item) {
        Intent intent = new Intent(getActivity(), ProdutoPorCategoriaActivity.class);
        intent.putExtra(getString(R.string.param_categoria), item);
        startActivity(intent);
    }

    private List<View> createPageList(List<Banner> banners) {
        List<View> pageList = new ArrayList();
        for(int i = 0; i < banners.size(); i++){
            pageList.add(createPageView(banners.get(i)));
        }

        return pageList;
    }

    private View createPageView(final Banner banner) {
        bannerAdapter = BannerViewBinding.inflate(getActivity().getLayoutInflater(), null, false);

        bannerAdapter.ivBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(banner.getLinkUrl())));
            }
        });

        Glide
                .with(this)
                .load(banner.getUrlImagem())
                .centerCrop()
                .placeholder(R.drawable.loading)
                .error(R.mipmap.ic_nao_disponivel)
                .into(bannerAdapter.ivBanner);

        return binding.getRoot();
    }
}
