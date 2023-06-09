package com.example.gigamall_app.tools

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.gigamall_app.R

class ShowPreviewFromThumb(
    private var expandedView: View,
    private var container: ViewGroup
) {
    private var currentAnimator: Animator? = null
    private var shortAnimationDuration: Int = 200
    private var startBounds : RectF = RectF()
    private var startScale : Float = 0.0f

    fun zoomIn(
        thumbView: View) {
        currentAnimator?.cancel()

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        val startBoundsInt = Rect()
        val finalBoundsInt = Rect()
        val globalOffset = Point()

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBoundsInt)
        container.getGlobalVisibleRect(finalBoundsInt, globalOffset)
        startBoundsInt.offset(-globalOffset.x, -globalOffset.y)
        finalBoundsInt.offset(-globalOffset.x, -globalOffset.y)

        val startBounds = RectF(startBoundsInt)
        val finalBounds = RectF(finalBoundsInt)

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        val startScale: Float
        if ((finalBounds.width() / finalBounds.height() > startBounds.width() / startBounds.height())) {
            // Extend start bounds horizontally
            startScale = startBounds.height() / finalBounds.height()
            val startWidth: Float = startScale * finalBounds.width()
            val deltaWidth: Float = (startWidth - startBounds.width()) / 2
            startBounds.left -= deltaWidth.toInt()
            startBounds.right += deltaWidth.toInt()
        } else {
            // Extend start bounds vertically
            startScale = startBounds.width() / finalBounds.width()
            val startHeight: Float = startScale * finalBounds.height()
            val deltaHeight: Float = (startHeight - startBounds.height()) / 2f
            startBounds.top -= deltaHeight.toInt()
            startBounds.bottom += deltaHeight.toInt()
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
//        thumbView.alpha = 0f

        expandedView.visibility = View.VISIBLE

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedView.pivotX = 0f
        expandedView.pivotY = 0f

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        currentAnimator = AnimatorSet().apply {
            play(
                ObjectAnimator.ofFloat(
                    expandedView,
                    View.X,
                    startBounds.left,
                    finalBounds.left)
            ).apply {
                with(ObjectAnimator.ofFloat(expandedView, View.Y, startBounds.top, finalBounds.top))
                with(ObjectAnimator.ofFloat(expandedView, View.SCALE_X, startScale, 1f))
                with(ObjectAnimator.ofFloat(expandedView, View.SCALE_Y, startScale, 1f))
            }
            duration = shortAnimationDuration.toLong()
            interpolator = DecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator) {
                    currentAnimator = null
                }

                override fun onAnimationCancel(animation: Animator) {
                    currentAnimator = null
                }
            })
            start()
        }
    }

    fun zoomOut(){
        currentAnimator?.cancel()

        // Animate the four positioning/sizing properties in parallel,
        // back to their original values.
        currentAnimator = AnimatorSet().apply {
            play(ObjectAnimator.ofFloat(expandedView, View.X, startBounds.left)).apply {
                with(ObjectAnimator.ofFloat(expandedView, View.Y, startBounds.top))
                with(ObjectAnimator.ofFloat(expandedView, View.SCALE_X, startScale))
                with(ObjectAnimator.ofFloat(expandedView, View.SCALE_Y, startScale))
            }
            duration = shortAnimationDuration.toLong()
            interpolator = DecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator) {
                    expandedView.visibility = View.GONE
                    currentAnimator = null
                }

                override fun onAnimationCancel(animation: Animator) {
                    expandedView.visibility = View.GONE
                    currentAnimator = null
                }
            })
            start()
        }
    }
}