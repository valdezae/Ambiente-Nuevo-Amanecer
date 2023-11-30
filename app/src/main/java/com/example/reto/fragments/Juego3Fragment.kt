package com.example.reto.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.navigation.findNavController
import com.example.reto.R
import com.mihir.drawingcanvas.drawingView

class Juego3Fragment : Fragment() {

    private lateinit var drawingView: drawingView
    private lateinit var btnJuego3Regresar : Button
    private lateinit var undobtn : ImageButton
    private lateinit var redobtn : ImageButton
    private lateinit var erasebtn : ImageButton
    private lateinit var clearbtn : ImageButton

    // Variables for color buttons
    private lateinit var btnBlack: Button
    private lateinit var btnWhite: Button
    private lateinit var btnRed: Button
    private lateinit var btnGreen: Button
    private lateinit var btnBlue: Button
    private lateinit var btnYellow: Button
    private lateinit var btnPink: Button
    private lateinit var btnPurple: Button
    private lateinit var btnOrange: Button
    private lateinit var btnBrown: Button
    private lateinit var btnCyan: Button
    private lateinit var btnGray: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_juego3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val colorBlack = requireContext().getColor(R.color.coloring_black)
        val colorWhite = requireContext().getColor(R.color.coloring_white)
        val colorRed = requireContext().getColor(R.color.coloring_red)
        val colorGreen = requireContext().getColor(R.color.coloring_green)
        val colorBlue = requireContext().getColor(R.color.coloring_blue)
        val colorYellow = requireContext().getColor(R.color.coloring_yellow)
        val colorPink = requireContext().getColor(R.color.coloring_pink)
        val colorPurple = requireContext().getColor(R.color.coloring_purple)
        val colorOrange = requireContext().getColor(R.color.coloring_orange)
        val colorBrown = requireContext().getColor(R.color.coloring_brown)
        val colorCyan = requireContext().getColor(R.color.coloring_cyan)
        val colorGray = requireContext().getColor(R.color.coloring_gray)
        val eraserW = requireContext().getColor((R.color.coloring_eraser))

        drawingView = view.findViewById(R.id.drawing_view)
        drawingView.setBrushColor(colorBlack)

        undobtn = view.findViewById(R.id.undobtn)
        undobtn.setOnClickListener {
            drawingView.undo()
        }

        redobtn = view.findViewById(R.id.redobtn)
        redobtn.setOnClickListener {
            drawingView.redo()
        }

        erasebtn = view.findViewById(R.id.eraserbtn)
        erasebtn.setOnClickListener {
            drawingView.erase(eraserW)
            drawingView.setSizeForBrush(30)
        }

        clearbtn = view.findViewById(R.id.clearbtn)
        clearbtn.setOnClickListener {
            drawingView.clearDrawingBoard()
        }

        // Initialize color buttons
        btnBlack = view.findViewById(R.id.btn_black)
        btnWhite = view.findViewById(R.id.btn_white)
        btnRed = view.findViewById(R.id.btn_red)
        btnGreen = view.findViewById(R.id.btn_green)
        btnBlue = view.findViewById(R.id.btn_blue)
        btnYellow = view.findViewById(R.id.btn_yellow)
        btnPink = view.findViewById(R.id.btn_pink)
        btnPurple = view.findViewById(R.id.btn_purple)
        btnOrange = view.findViewById(R.id.btn_orange)
        btnBrown = view.findViewById(R.id.btn_brown)
        btnCyan = view.findViewById(R.id.btn_cyan)
        btnGray = view.findViewById(R.id.btn_gray)

        // Set click listeners or perform actions with these color buttons as needed
// Set click listeners for color buttons with brush size
        btnBlack.setOnClickListener {
            drawingView.setBrushColor(colorBlack)
            drawingView.setSizeForBrush(20)
        }

        btnWhite.setOnClickListener {
            drawingView.setBrushColor(colorWhite)
            drawingView.setSizeForBrush(20)
        }

        btnRed.setOnClickListener {
            drawingView.setBrushColor(colorRed)
            drawingView.setSizeForBrush(20)
        }

        btnGreen.setOnClickListener {
            drawingView.setBrushColor(colorGreen)
            drawingView.setSizeForBrush(20)
        }

        btnBlue.setOnClickListener {
            drawingView.setBrushColor(colorBlue)
            drawingView.setSizeForBrush(20)
        }

        btnYellow.setOnClickListener {
            drawingView.setBrushColor(colorYellow)
            drawingView.setSizeForBrush(20)
        }

        btnPink.setOnClickListener {
            drawingView.setBrushColor(colorPink)
            drawingView.setSizeForBrush(20)
        }

        btnPurple.setOnClickListener {
            drawingView.setBrushColor(colorPurple)
            drawingView.setSizeForBrush(20)
        }

        btnOrange.setOnClickListener {
            drawingView.setBrushColor(colorOrange)
            drawingView.setSizeForBrush(20)
        }

        btnBrown.setOnClickListener {
            drawingView.setBrushColor(colorBrown)
            drawingView.setSizeForBrush(20)
        }

        btnCyan.setOnClickListener {
            drawingView.setBrushColor(colorCyan)
            drawingView.setSizeForBrush(20)
        }

        btnGray.setOnClickListener {
            drawingView.setBrushColor(colorGray)
            drawingView.setSizeForBrush(20)
        }


        btnJuego3Regresar = view.findViewById(R.id.btn_Juego_3_Regresar)
        btnJuego3Regresar.setOnClickListener {
            it.findNavController().navigate(R.id.action_juego3Fragment_to_nivelesFragment)
        }

    }
}