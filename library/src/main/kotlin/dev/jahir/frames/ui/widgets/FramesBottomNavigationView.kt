package dev.jahir.frames.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout
import androidx.annotation.IdRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.jahir.frames.R
import dev.jahir.frames.extensions.context.resolveColor

class FramesBottomNavigationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BottomNavigationView(context, attrs, defStyleAttr) {

    init {
        init(context, attrs)
        
        // 1. Убираем системные отступы (те самые 24dp снизу)
        ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
            insets // Просто возвращаем инсеты, не применяя их к padding
        }

        // 2. Центрируем внутреннее меню
        post {
            centerMenu()
        }
    }

    private fun init(context: Context, attributeSet: AttributeSet?) {
        val a = context.obtainStyledAttributes(
            attributeSet, R.styleable.FramesBottomNavigationView, 0, 0
        )
        try {
            val forceRightColor =
                a.getBoolean(R.styleable.FramesBottomNavigationView_forceRightColor, false)
            if (forceRightColor)
                setBackgroundColor(context.resolveColor(com.google.android.material.R.attr.colorSurface))
        } finally {
            a.recycle()
        }
        
        // Настройка для Floating-вида: убираем подписи, чтобы иконки были по центру
        labelVisibilityMode = LABEL_VISIBILITY_UNLABELED
    }

    private fun centerMenu() {
        // В BottomNavigationView есть дочерний элемент, который держит иконки
        val menuView = getChildAt(0) as? BottomNavigationMenuView
        menuView?.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        ).apply {
            gravity = Gravity.CENTER // Центрируем всё содержимое
        }
    }

    // Переопределяем метод, чтобы система не могла насильно добавить padding
    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, 0) // Всегда зануляем bottom
    }

    fun setSelectedItemId(@IdRes itemId: Int, triggerEvent: Boolean = true) {
        try {
            if (triggerEvent) super.setSelectedItemId(itemId)
            else menu.findItem(itemId)?.isChecked = true
        } catch (e: Exception) {
        }
    }

    fun setItemVisible(@IdRes itemId: Int, visible: Boolean) {
        try {
            menu.findItem(itemId).setVisible(visible)
        } catch (e: Exception) {
        }
    }
}
