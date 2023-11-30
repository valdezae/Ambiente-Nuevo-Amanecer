package com.example.reto.fragments

import android.content.ClipData
import android.content.ClipDescription
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.reto.R
import com.example.reto.databinding.FragmentJuego2Binding
import kotlin.random.Random

class Juego2Fragment : Fragment() {

    private lateinit var binding: FragmentJuego2Binding
    private val shapeSlotMap = mapOf(
        R.id.dragViewSquare to R.id.llBottomSquare,
        R.id.dragViewCircle to R.id.llBottomCircle,
        R.id.dragViewTriangle to R.id.llBottomTriangle,
        R.id.dragViewStar to R.id.llBottomStar,
        R.id.dragViewHeart to R.id.llBottomHeart
    )



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_juego2, container, false)

        binding.backBtn.setOnClickListener{
            it.findNavController().navigate(R.id.action_juego2Fragment_to_nivelesFragment)
        }

        binding.llTop.setOnDragListener(dragListener)
        binding.llBottomSquare.setOnDragListener(dragListener)
        binding.llBottomStar.setOnDragListener(dragListener)
        binding.llBottomHeart.setOnDragListener(dragListener)
        binding.llBottomTriangle.setOnDragListener(dragListener)
        binding.llBottomCircle.setOnDragListener(dragListener)

        binding.dragViewCircle.setOnLongClickListener{
            val clipText = "circle"
            val item = ClipData.Item(clipText)
            val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val data = ClipData(clipText, mimeTypes, item)

            val dragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(data, dragShadowBuilder, it, 0)

            it.visibility = View.VISIBLE
            true
        }

        binding.dragViewSquare.setOnLongClickListener{
            val clipText = "square"
            val item = ClipData.Item(clipText)
            val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val data = ClipData(clipText, mimeTypes, item)

            val dragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(data, dragShadowBuilder, it, 0)

            it.visibility = View.VISIBLE
            true
        }

        binding.dragViewHeart.setOnLongClickListener{
            val clipText = "heart"
            val item = ClipData.Item(clipText)
            val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val data = ClipData(clipText, mimeTypes, item)

            val dragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(data, dragShadowBuilder, it, 0)

            it.visibility = View.VISIBLE
            true
        }

        binding.dragViewStar.setOnLongClickListener{
            val clipText = "star"
            val item = ClipData.Item(clipText)
            val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val data = ClipData(clipText, mimeTypes, item)

            val dragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(data, dragShadowBuilder, it, 0)

            it.visibility = View.VISIBLE
            true
        }

        binding.dragViewTriangle.setOnLongClickListener{
            val clipText = "triangle"
            val item = ClipData.Item(clipText)
            val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val data = ClipData(clipText, mimeTypes, item)

            val dragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(data, dragShadowBuilder, it, 0)

            it.visibility = View.VISIBLE
            true
        }


        return binding.root
    }

    val dragListener = View.OnDragListener(){ view, event ->
        when(event.action){
            DragEvent.ACTION_DRAG_STARTED -> {
                event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
                true
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                view.invalidate()
                true
            }
            DragEvent.ACTION_DRAG_LOCATION -> true
            DragEvent.ACTION_DRAG_EXITED -> {
                view.invalidate()
                true
            }
            DragEvent.ACTION_DROP -> {
                val item = event.clipData.getItemAt(0)
                val dragData = item.text
                val v = event.localState as View
                val owner = v.parent as ViewGroup
                val destination = view as LinearLayout
                if (destination.tag == dragData.toString()) {
                    // Correct slot, keep the shape in the slot
                    owner.removeView(v)
                    destination.removeAllViews()
                    destination.addView(v)
                    v.visibility = View.VISIBLE

                    // Check if all shapes are in the correct slots (win condition)
                    if(checkWinCondition()){
                        restartGame()
                    }
                } else {
                    // Incorrect slot, move the shape back to the original position
                    Toast.makeText(context, "Figura en ranura incorrecta ${destination.tag} ${dragData.toString()}", Toast.LENGTH_SHORT).show()
                    v.visibility = View.VISIBLE
                }

                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                view.invalidate()
                true
            }
            else -> false
        }
    }

    private fun checkWinCondition(): Boolean {
        var allCorrect = true

        for ((shapeId, expectedSlotId) in shapeSlotMap) {
            val shapeView = binding.root.findViewById<View>(shapeId)
            val expectedSlot = binding.root.findViewById<View>(expectedSlotId)

            // Check if the shape is in the correct slot
            if (shapeView.parent !== expectedSlot) {
                allCorrect = false
                break
            }

        }
        return allCorrect


    }

    private fun restartGame() {
        // Reset the UI and game state here

        // Show a message or perform any other actions
        Toast.makeText(context, "Congratulations! You win! Restarting the game.", Toast.LENGTH_SHORT).show()

        // Reset the visibility and position of shape views
        binding.dragViewSquare.visibility = View.VISIBLE
        binding.dragViewCircle.visibility = View.VISIBLE
        binding.dragViewTriangle.visibility = View.VISIBLE
        binding.dragViewStar.visibility = View.VISIBLE
        binding.dragViewHeart.visibility = View.VISIBLE

        // Remove the shape views from their current parent containers
        (binding.dragViewSquare.parent as? ViewGroup)?.removeView(binding.dragViewSquare)
        (binding.dragViewCircle.parent as? ViewGroup)?.removeView(binding.dragViewCircle)
        (binding.dragViewTriangle.parent as? ViewGroup)?.removeView(binding.dragViewTriangle)
        (binding.dragViewStar.parent as? ViewGroup)?.removeView(binding.dragViewStar)
        (binding.dragViewHeart.parent as? ViewGroup)?.removeView(binding.dragViewHeart)

        // Reset the position of shape views to the initial layout
        binding.llTop.addView(binding.dragViewSquare)
        binding.llTop.addView(binding.dragViewCircle)
        binding.llTop.addView(binding.dragViewTriangle)
        binding.llTop.addView(binding.dragViewStar)
        binding.llTop.addView(binding.dragViewHeart)

        // Reset the position of slot views to the initial layout
        binding.llBottomSquare.addView(binding.squareSlot)
        binding.llBottomCircle.addView(binding.circleSlot)
        binding.llBottomTriangle.addView(binding.triangleSlot)
        binding.llBottomStar.addView(binding.starSlot)
        binding.llBottomHeart.addView(binding.heartSlot)

        // You may need to reinitialize or reset any game-related variables or views here
    }




}






