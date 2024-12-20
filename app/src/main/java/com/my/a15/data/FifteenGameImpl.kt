package com.my.a15.data

import com.my.a15.domain.ColorCell
import com.my.a15.domain.MyCell
import com.my.a15.domain.MyModelNum

class FifteenGameImpl : FifteenGame {
    private val finalState = (1..15) + null
    override fun getStartGameModel(): MyModelNum {
        return getStartModel()
    }

    override fun replaceElement(
        myModelNum: MyModelNum,
        indexItem: Int,
        indexNull: Int
    ): MyModelNum {
        val oldList = myModelNum.listCells
        val newList = swap(oldList, indexItem, indexNull)
        val isVictory = newList.map { it?.num } == finalState
        return myModelNum.copy(
            isVictory = isVictory,
            countStep = myModelNum.countStep + 1,
            listCells = newList
        )
    }

    /** OTHER METHOD ____________________________________________________________________________*/

    private fun getStartModel(): MyModelNum {
        val listModel = getListInt().mapIndexed { index, intValue ->
            intValue?.let {
                val color = getColorCorrectPosition(intValue, index)
                MyCell(num = it, colorCell = color)
            }
        }
        return MyModelNum(isVictory = false, countStep = 0, listCells = listModel)
    }

    private fun getColorCorrectPosition(intValue: Int, index: Int): ColorCell {
        return if (intValue == finalState[index]) ColorCell.CORRECT_POSITION
        else ColorCell.DEFAULT

    }

    private fun getListInt(): List<Int?> {
        val mutableList = finalState.toMutableList()
        // два дня на коленях умолял gpt сделать код
        fun isSolvable(tiles: List<Int?>): Boolean {
            val flattened = tiles.filterNotNull() // Убираем null
            val inversions = flattened.indices.sumBy { i ->
                (i + 1 until flattened.size).count { j -> flattened[i] > flattened[j] }
            }
            // Находим позицию пустой клетки (null)
            val gridSize =
                Math.sqrt(tiles.size.toDouble()).toInt() // Размер сетки (например, 4x4)
            val nullRow = tiles.indexOf(null) / gridSize // Ряд, где находится null

            // Для четного размера поля нужно учитывать как число инверсий, так и позицию пустой клетки
            return if (gridSize % 2 == 1) {
                inversions % 2 == 0 // Если размер сетки нечётный, конфигурация решаема, если инверсий чётное число
            } else {
                (inversions + nullRow) % 2 == 1 // Для чётного размера сетки решаемость зависит от суммы инверсий и позиции null
            }
        }
        do {
            mutableList.shuffle() // Перемешиваем список
        } while (!isSolvable(mutableList)) // Проверяем, решаемая ли конфигурация
        //_____________________________________________________________________________________
        return mutableList
    }

    private fun swap(oldList: List<MyCell?>, indexItem: Int, indexNull: Int): List<MyCell?> {
        val newList = oldList.toMutableList()
        newList[indexItem] = newList[indexNull].also { newList[indexNull] = newList[indexItem] }
        newList[indexNull] = newList[indexNull]?.let { myCell ->
            val color = getColorCorrectPosition(intValue = myCell.num, index = indexNull)
            myCell.copy(colorCell = color)
        }

        return newList.toList()
    }
}