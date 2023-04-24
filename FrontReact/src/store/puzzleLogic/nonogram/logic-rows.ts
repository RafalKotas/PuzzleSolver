import { arrayReversedNumber, arrayReversedString, createArrayOfEmptyFields, findLastIndexContaining, generateArrayOfSequenceMarks } from "./logic"

// row ranges
export const inferInitialRowsSequencesRanges = (rowsSequences : Array<Array<number>>, board: Array<Array<string>>) => {
    return rowsSequences.map((rowSequences, rowIdx) => {
        return inferInitialRowSequencesRanges(rowSequences, rowIdx, board)
    })
}

const inferInitialRowSequencesRanges = (sequences: Array<number>, rowIdx : number, board: Array<Array<string>>) => {

    let arrayFilledFromStart = createRowArrayFromSequencesAndChars(rowIdx, sequences, board, false)
    let arrayFilledFromEnd = arrayReversedString(createRowArrayFromSequencesAndChars(rowIdx, sequences, board, true))

    return inferRowSequencesRangesFromArrays(arrayFilledFromStart, arrayFilledFromEnd)
}

const createRowArrayFromSequencesAndChars = (rowIdx : number, sequencesParam: number[], board : string[][], reverse : boolean) => {
    
    let sequences = sequencesParam
    let charsNeeded =  generateArrayOfSequenceMarks(sequences.length)

    if(reverse) {
        sequences = arrayReversedNumber(sequences)
        charsNeeded = arrayReversedString(charsNeeded)
    }

    let width = board[0].length
    let arrayFilledFromStart = createArrayOfEmptyFields(width)
    
    let canStartSequenceFromIndex = false
    let writeSequenceMode = false
    let currentSequenceIdx = 0
    let sequencesFieldsFilled = 0
    let charToWrite = charsNeeded[ currentSequenceIdx ]
    let sequenceLength = sequences[ currentSequenceIdx ]
    let breakX = true
    
    for(let fieldIdx = 0; fieldIdx < width; fieldIdx++ ) {
        
        if(!writeSequenceMode && currentSequenceIdx < charsNeeded.length && breakX) {
            canStartSequenceFromIndex = checkIfCanStartSequenceFromRowIndex(rowIdx, fieldIdx, sequenceLength, board)
            if(canStartSequenceFromIndex) {
                writeSequenceMode = true // start fill fields with sequence char mark
            }
        }
        if(writeSequenceMode) {
            
            arrayFilledFromStart[ fieldIdx ] = "R" + charToWrite + board[ rowIdx ][ fieldIdx ].substring(2, 4)
            
            sequencesFieldsFilled++
            
            if(sequencesFieldsFilled === sequenceLength) {
                sequencesFieldsFilled = 0
                currentSequenceIdx++
                charToWrite = charsNeeded[ currentSequenceIdx ]
                sequenceLength = sequences[ currentSequenceIdx ]
                writeSequenceMode = false
                breakX = false
            }
        } else {
            arrayFilledFromStart[ fieldIdx ] = "XXXX"
            breakX = true
        }
    }

    return arrayFilledFromStart
}

// check if in range  [fieldIdx, fieldIdx + sequenceLength - 1] are not "XXXX"
const checkIfCanStartSequenceFromRowIndex = (rowIdx : number, fieldIdx : number, sequenceLength: number, board: Array<Array<string>>) => {
    let fieldsToCheck = board[ rowIdx ].slice(fieldIdx, fieldIdx + sequenceLength)
    let xs = fieldsToCheck.filter((field) => field === "XXXX")
    return xs.length === 0
}

const inferRowSequencesRangesFromArrays = (arrayFilledFromStart : string[], arrayFilledFromEnd : string[]) => {
    let collectedSequences = [] as string[]
    let sequencesRanges = [] as number[][]
    let rangeStartIndex : number
    let rangeLastIndex : number

    arrayFilledFromStart.forEach((field, index) => {
        let fieldSequenceChar = field.charAt(1)
        if(field.startsWith("R") && !collectedSequences.includes(fieldSequenceChar)) {
            collectedSequences.push(fieldSequenceChar)
            rangeStartIndex = index
            rangeLastIndex = findLastIndexContaining(arrayFilledFromEnd, "R" + fieldSequenceChar)
            sequencesRanges.push([rangeStartIndex, rangeLastIndex])
        }
    })

    return sequencesRanges
}