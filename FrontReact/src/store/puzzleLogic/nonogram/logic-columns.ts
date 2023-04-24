import { arrayReversedNumber, arrayReversedString, createArrayOfEmptyFields, findLastIndexContaining, generateArrayOfSequenceMarks } from "./logic"

// columns sequences ranges
export const inferInitialColumnsSequencesRanges = (columnsSequences : Array<Array<number>>, board: Array<Array<string>>) => {
    return columnsSequences.map((columnsSequences, columnIdx) => {
        return inferInitialColumnSequencesRanges(columnsSequences, columnIdx, board)
    })
}

const inferInitialColumnSequencesRanges = (sequences: Array<number>, columnIdx : number, board: Array<Array<string>>) => {

    let arrayFilledFromStart = createColumnArrayFromSequencesAndChars(columnIdx, sequences, board, false)
    let arrayFilledFromEnd = arrayReversedString(createColumnArrayFromSequencesAndChars(columnIdx, sequences, board, true))

    return inferColumnSequencesRangesFromArrays(arrayFilledFromStart, arrayFilledFromEnd)
}

const createColumnArrayFromSequencesAndChars = (columnIdx : number, sequencesParam: number[], board : string[][], reverse : boolean) => {
    
    let sequences = sequencesParam
    let charsNeeded =  generateArrayOfSequenceMarks(sequences.length)

    if(reverse) {
        sequences = arrayReversedNumber(sequences)
        charsNeeded = arrayReversedString(charsNeeded)
    }

    let height = board.length
    let arrayFilledFromStart = createArrayOfEmptyFields(height)
    
    let canStartSequenceFromIndex = false
    let writeSequenceMode = false
    let currentSequenceIdx = 0
    let sequencesFieldsFilled = 0
    let charToWrite = charsNeeded[ currentSequenceIdx ]
    let sequenceLength = sequences[ currentSequenceIdx ]
    let breakX = true
    
    for(let fieldIdx = 0; fieldIdx < height; fieldIdx++ ) {
        
        if(!writeSequenceMode && currentSequenceIdx < charsNeeded.length && breakX) {
            canStartSequenceFromIndex = checkIfCanStartSequenceFromColumnIndex(columnIdx, fieldIdx, sequenceLength, board)
            if(canStartSequenceFromIndex) {
                writeSequenceMode = true // start fill fields with sequence char mark
            }
        }
        if(writeSequenceMode) {
            
            arrayFilledFromStart[ fieldIdx ] = "C" + charToWrite + board[ fieldIdx ][ columnIdx ].substring(2, 4)
            
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
const checkIfCanStartSequenceFromColumnIndex = (columnIdx : number, fieldIdx : number, sequenceLength: number, board: Array<Array<string>>) => {
    let columnFields = board.map((boardRow) => boardRow[columnIdx])
    let xs = columnFields.filter((field) => field === "XXXX")
    return xs.length === 0
}

const inferColumnSequencesRangesFromArrays = (arrayFilledFromStart : string[], arrayFilledFromEnd : string[]) => {
    let collectedSequences = [] as string[]
    let sequencesRanges = [] as number[][]
    let rangeStartIndex : number
    let rangeLastIndex : number

    arrayFilledFromStart.forEach((field, index) => {
        let fieldSequenceChar = field.charAt(1)
        if(field.startsWith("C") && !collectedSequences.includes(fieldSequenceChar)) {
            collectedSequences.push(fieldSequenceChar)
            rangeStartIndex = index
            rangeLastIndex = findLastIndexContaining(arrayFilledFromEnd, "C" + fieldSequenceChar)
            sequencesRanges.push([rangeStartIndex, rangeLastIndex])
        }
    })

    return sequencesRanges
}