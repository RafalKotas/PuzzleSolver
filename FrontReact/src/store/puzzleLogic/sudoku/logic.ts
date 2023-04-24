import commonFunctions from "../../../functions"
import { markedCell } from "./types"

export const square3x3cellsIndices = (rowIndex: number, columnIndex: number) => {
    let [square3x3RowStart, square3x3ColumnStart] = [ Math.floor(rowIndex / 3) * 3, Math.floor(columnIndex / 3) * 3] 

    // example:  rI = 4, cI = 4 -> s3x3RS = 3, s3x3CS = 3, 
    // sC = [[[3, 3], [3, 4], [3, 5]], [[4, 3], [4, 4], [4, 5]], [[5, 3], [5, 4], [5, 5]]].flat()
    return Array.from({ length: 3 }, (_, rowIdx) => {
        return Array.from({length: 3}, (_, columnIdx) => {
            return [square3x3RowStart + rowIdx, square3x3ColumnStart + columnIdx]
        })
    }).flat()
}

// adds available option in all cells which row is same as removed digit (mode === "CREATE", action RemoveDigitFromCellEditMode)
export const addDigitOptionInRow = (arr3d : number[][][], createdSudokuBoard: number[][], digitToAdd : number, rowToAdd: number) => {
    let addedInRow = arr3d.map((rowNumbers, nthRowIdx) => {
        //if 'row' !== 'row with removed digit solution board' => return old, else return row with added this digit
        return nthRowIdx === rowToAdd ? rowNumbers.map((nthRowArray, nthColIdx) => {
            return createdSudokuBoard[ nthRowIdx ][ nthColIdx ] === 0 ? [ ...nthRowArray, digitToAdd ].sort() : nthRowArray
        }) : rowNumbers 
    })
    return addedInRow
}


export const addDigitOptionInColumn = (arr3d : number[][][], createdSudokuBoard: number[][], digitToAdd : number, columnToAdd : number) => {
    return arr3d.map((rowNumbers, rowIndex) => {
        return rowNumbers.map((numbersAvailableInColumn, columnIndex) => {
            //if 'column' !== 'column with removed digit solution board' => return old, else return column with added this digit(sorted)
            return (columnIndex !== columnToAdd || createdSudokuBoard[ rowIndex ][ columnIndex ] !==0)  ? numbersAvailableInColumn : [...numbersAvailableInColumn, digitToAdd].sort()
        })
    })
}

export const addDigitOptionInSquare3x3 = (arr3d : number[][][], createdSudokuBoard: number[][], digitToAdd : number, rowToAdd : number, columnToAdd : number) => {

    //find indices (set of [rowIdx, columnIdx]) of 3x3 square
    let squaresCoordsToAddDigit = square3x3cellsIndices(rowToAdd, columnToAdd)

    return arr3d.map((rowNumbers, rowIndex) => {
        
        return rowNumbers.map((numbersAvailable, columnIndex) => {
            
            //find squares (from board 9x9) matching cells coordinates from squaresCoordsToAddDigit
            return (squaresCoordsToAddDigit.filter(([bigSquareRowIndex, bigSquareColumnIndex]) => {
                return (bigSquareRowIndex === rowIndex && bigSquareColumnIndex === columnIndex && createdSudokuBoard[ rowIndex ][ columnIndex ] === 0)
            }).length === 1) ? [ ...numbersAvailable.filter((numb) => numb !== digitToAdd ), digitToAdd ].sort() : numbersAvailable
        })

    })
}

export const generateCellsCoordsAffectingCell = (rowToAdd: number, columnToAdd : number) => {
    let coordsInRow = Array.from({length: 9}, (_, colIdx) => {
        return [rowToAdd, colIdx]
    })
    let coordsInColumn = Array.from({length: 9}, (_, rowIdx) => {
        return [rowIdx, columnToAdd]
    })
    let coordsInSquare3x3 = square3x3cellsIndices(rowToAdd, columnToAdd)

    return coordsInRow.concat(coordsInColumn).concat(coordsInSquare3x3)
}

export const matchIndicesToNotAllowedNumbersSet = (createdSudokuBoard: number[][], cellsCoords : number[][]) => {
    
    let numbersNotAllowedInCellSorted = cellsCoords.reduce((prevValue, currentValue) => {
        let [rowIdx, colIdx] = currentValue
        let digitOnBoard = createdSudokuBoard[ rowIdx ][ colIdx ]
        return digitOnBoard !== 0 ? prevValue.concat(digitOnBoard) : prevValue
    }, [])

    return numbersNotAllowedInCellSorted.length > 0 ? Array.from(new Set(numbersNotAllowedInCellSorted)) : numbersNotAllowedInCellSorted
}

export const generateNumbersAllowedInCell = (rowIdx: number, columnIdx : number, createdSudokuBoard: number[][]) => {
    let cordsAffectingCell = generateCellsCoordsAffectingCell(rowIdx, columnIdx)
    let notAllowedNumberSet = matchIndicesToNotAllowedNumbersSet(createdSudokuBoard, cordsAffectingCell)
    let digitsAllowed = Array.from({length: 9}, (_, value) => {
        return value + 1
    }).filter((digit) => {
        return !notAllowedNumberSet.includes(digit)
    })
    return digitsAllowed
}

export const generateNumbersAllowedInEveryBoardCell = (createdSudokuBoard: number[][]) => {
    return createdSudokuBoard.map((row, rowIdx) => {
        return row.map((_, columnIdx) => {
            return generateNumbersAllowedInCell(rowIdx, columnIdx, createdSudokuBoard).concat(createdSudokuBoard[rowIdx][columnIdx])
        })
    })
}


export const addDigitOption = (arr3d : number[][][], createdSudokuBoard: number[][], digitToAdd : number, rowToAdd : number, columnToAdd : number) => {
    let addedInRow = addDigitOptionInRow(arr3d, createdSudokuBoard, digitToAdd, rowToAdd )
    let addedInRowAndColumn = addDigitOptionInColumn(addedInRow, createdSudokuBoard, digitToAdd, columnToAdd)
    let addedInRowColumnAndSquare3x3 = addDigitOptionInSquare3x3(addedInRowAndColumn, createdSudokuBoard, digitToAdd, rowToAdd, columnToAdd)
    return addedInRowColumnAndSquare3x3
}

// removes available option in all cells which row is same as inserted digit
export const removeDigitOptionInRow = (arr3d : number[][][], digitToRemove : number, rowToRemove : number) => {
    return arr3d.map((rowNumbers, nthRowIdx) => {
        return nthRowIdx !== rowToRemove ? rowNumbers : rowNumbers.map((nthRowArray, _) => {
            return nthRowArray.filter((digit) => digit !== digitToRemove)
        })
    })
}

export const removeDigitOptionInColumn = (arr3d : number[][][], digitToRemove : number, columnToRemove : number) => {
    return arr3d.map((rowNumbers, _) => {
        return rowNumbers.map((numbersAvailableInColumn, columnIndex) => {
            return columnIndex !== columnToRemove ? numbersAvailableInColumn : numbersAvailableInColumn.filter((digit) => {
                return digit !== digitToRemove
            })
        })
    })
}

export const removeDigitOptionInSquare3x3 = (arr3d : number[][][], digitToRemove : number, rowToRemove : number, columnToRemove : number) => {

    let squaresCoordsToRemoveDigit = square3x3cellsIndices(rowToRemove, columnToRemove)

    return arr3d.map((rowNumbers, rowIndex) => {
        
        return rowNumbers.map((numbersAvailable, columnIndex) => {
            
            //find squares matching bigSquareIndices, filter cells returning numbers without digit
            return (squaresCoordsToRemoveDigit.filter(([bigSquareRowIndex, bigSquareColumnIndex]) => {
                return (bigSquareRowIndex === rowIndex && bigSquareColumnIndex === columnIndex)
            }).length === 1) ? numbersAvailable.filter((digit) => digit !== digitToRemove) : numbersAvailable
        })

    })
}

export const removeDigitOption = (arr3d : number[][][], digitToRemove : number, rowToRemove : number, columnToRemove : number) => {
    let removedInRow = removeDigitOptionInRow(arr3d, digitToRemove, rowToRemove )
    let removedInRowAndColumn = removeDigitOptionInColumn(removedInRow, digitToRemove, columnToRemove)
    return removeDigitOptionInSquare3x3(removedInRowAndColumn, digitToRemove, rowToRemove, columnToRemove)
}


export const onlyPossibleOptionInCellCoordinatesAndValuesList = (arr3d: number[][][], rowsRange: number[], columnsRange: number[]) => arr3d.map((availableCellsNumbersInRow, rowIndex) => {
    return availableCellsNumbersInRow.map((numbersAvailableInCell, columnIndex) => {
        return onlyPossibleOptionInCellConditon(numbersAvailableInCell, rowIndex, columnIndex, rowsRange, columnsRange)  ? [numbersAvailableInCell[0], [rowIndex, columnIndex]] : []
    }).filter((cellWithDigitAndCoordinatesOrEmpty) => {
        return cellWithDigitAndCoordinatesOrEmpty.length > 0
    })
}).flat()

const onlyPossibleOptionInCellConditon = (numbersAvailableInCell : number[], rowIndex: number, columnIndex: number, rowsRangeToFindOnlyPossibleOption : number[], columnsRangeToFindOnlyPossibleOption: number[]) => {
    let onlyPossibleOption = numbersAvailableInCell.length === 1
    let rangeIncludingRow = commonFunctions.isValueInRange(rowIndex, rowsRangeToFindOnlyPossibleOption) || rowsRangeToFindOnlyPossibleOption[0] === -1
    let rangeIncludingColumn = commonFunctions.isValueInRange(columnIndex, columnsRangeToFindOnlyPossibleOption) || columnsRangeToFindOnlyPossibleOption[0] === -1
    return onlyPossibleOption && rangeIncludingRow && rangeIncludingColumn
}

export const fillSudokuWithMarkedCells = (sudokuBoard: number[][], markedCells: markedCell[]) => {
    let updatedSudokuBoard = sudokuBoard
    markedCells.forEach((markedCell) => {
        let {digit, cellCoords} = markedCell
        let { rowIdx, columnIdx } = cellCoords
        updatedSudokuBoard[ rowIdx ][ columnIdx ] = digit
    })
    return updatedSudokuBoard
}

export const calculateFilledFields = (sudokuBoard: number[][]) => {
    return sudokuBoard.reduce((prevValue, currentRow) => {
        return prevValue + currentRow.filter((digit) => digit !== 0).length
    }, 0)
}