package co.a3tecnology.testapp.viewPager

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import co.a3tecnology.testapp.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ViewPager : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    private lateinit var banners: Array<String>
    private lateinit var adapter: BannerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pager)
//
//        supportFragmentManager.beginTransaction()
//            .add(R.id.container, BannerFragment.getInstance(1)).commit()

        viewPager = findViewById(R.id.view_pager)

        tabLayout = findViewById(R.id.tab_Layout)


        banners = resources.getStringArray(R.array.banners)
        adapter = BannerAdapter(this,  banners.size)

        viewPager.adapter = adapter

        //ouvir evento de paginação
        viewPager.registerOnPageChangeCallback(callback)

        //scrolling vertical
//        viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL

        //tablayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = banners[position].split(".")[0]

        }.attach()

        //visualização da direta para esquerda
//        viewPager.layoutDirection = ViewPager2.LAYOUT_DIRECTION_RTL
//        tabLayout.layoutDirection = View.LAYOUT_DIRECTION_RTL

        //animacao da tab
//        viewPager.setPageTransformer { page, position ->
//            println("$position")
//
//            //pegar o numero absoluto para ter a posiçao positiva
//            val ref = 1 - abs(position)
//
//            //scala
//            page.scaleY = (0.85f + ref * 0.15f)
//        }

        //ira mostra 3 imagens - sendo uma na principal e duas lateriais
        viewPager.offscreenPageLimit = 3
        viewPager.clipToPadding = false // ira corta as arestas lateriais
        viewPager.clipChildren = false // ira corta a views filho

        //padding entre os elementos - irá adicionar multiplas transformações na página
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer  { page, position ->
            println("$position")

            //pegar o numero absoluto para ter a posiçao positiva
            val ref = 1 - Math.abs(position)

            //scala
            page.scaleY = (0.85f + ref * 0.15f)
        }

        //transformação de imagem
        compositePageTransformer.addTransformer(MarginPageTransformer(40))

        //irá adicionar as alteraçoes ao viewpager
        viewPager.setPageTransformer(compositePageTransformer)

    }

    //criar animacao das fotos
    private val slider = Handler()

    // ação que fará o slide passar para a próxima tab
    private val runnable = Runnable {
        viewPager.currentItem = viewPager.currentItem + 1
    }

    private val callback
        get() =  object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                Toast.makeText(
                    this@ViewPager, "test $position", Toast.LENGTH_SHORT
                ).show()

                //rolagem de banners infinita
                if ((banners.size - 2) == position) {
                    banners += resources.getStringArray(R.array.banners)
                    adapter.itemsCount = banners.size
                    adapter.notifyDataSetChanged()
                }
                //retira o slide principal da acao
                slider.removeCallbacks(runnable)

                //ira adicionar um novo slide como principal + tempo
                slider.postDelayed(runnable, 1500)
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        viewPager.unregisterOnPageChangeCallback(callback)
    }


    //adapter para atender a ViewPager2 / diferente da RecycleView
    inner class BannerAdapter(
        activity: AppCompatActivity,
        var itemsCount: Int) : FragmentStateAdapter(activity) {

        override fun getItemCount() = itemsCount

        override fun createFragment(position: Int): Fragment {

            val filename = banners[position]
            return BannerFragment.getInstance(filename)
        }
    }
}