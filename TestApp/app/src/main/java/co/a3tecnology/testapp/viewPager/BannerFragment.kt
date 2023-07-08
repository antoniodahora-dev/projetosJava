package co.a3tecnology.testapp.viewPager

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import co.a3tecnology.testapp.R

class BannerFragment : Fragment() {

    companion object {
        fun getInstance(filename: String) : BannerFragment {
            return BannerFragment().apply {
                arguments = Bundle().apply {
                    putString("filename", filename)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.frag_banner, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

//        val position = requireArguments().getInt("position")
//        val banners = resources.getStringArray(R.array.banners)
//        val filename = banners[position]

        val filename = requireArguments().getString("filename")!!

        val file = resources.assets.open(filename)
        val bitmap = BitmapFactory.decodeStream(file)
        file.close()

        val img = requireView().findViewById<ImageView>(R.id.img)

        img.setImageBitmap(bitmap)

    }
}