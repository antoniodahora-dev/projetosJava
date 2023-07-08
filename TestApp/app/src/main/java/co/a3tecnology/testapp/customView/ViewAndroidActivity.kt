package co.a3tecnology.testapp.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import co.a3tecnology.testapp.viewPager.BannerFragment
import co.a3tecnology.testapp.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class ViewAndroidActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_android)

//        val textView = TextView(this)
//        textView.text = "Olá Mundo!"
//        setContentView(textView)
    }
}

//Views Customizadas
class MyView : View {

    // Usa com código
    constructor(context: Context?) : super(context) {
        init()
        println("1")
    }

    // Usa com XML
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()

        val typedArray = context?.theme?.obtainStyledAttributes(attrs, R.styleable.MyView,
            0, 0)

       typedArray?.let {
           color = it.getColor(R.styleable.MyView_myColor, Color.BLUE)
       }
        typedArray?.recycle()
        println("2")
    }

    // Usa com XML (temas)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    // Usa com XML (temas / styleRes)
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private var size = 300 // raio do circulo
    private val pain = Paint(Paint.ANTI_ALIAS_FLAG) // pinta
    private var color = Color.RED // cor

    //desenha sobre a View
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        pain.color = color
        pain.style = Paint.Style.FILL

        val radius = size / 2f

        val x = (sin((System.currentTimeMillis()) % 1000 / 1000.0 * Math.PI * 2) * 100)
        val y = (cos((System.currentTimeMillis()) % 1000 / 1000.0 * Math.PI * 2) * 50)

        canvas?.drawCircle((size) + x.toFloat(), (size) + y.toFloat(), radius, pain)
        invalidate()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//        size = min(measuredWidth, measuredHeight)
//        setMeasuredDimension(size, size)
    }

    private fun init() {
        setBackgroundColor(Color.parseColor("#FF00FF00"))
    }
}
