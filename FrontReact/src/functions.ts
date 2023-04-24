import { selectedNonogramDetails } from "./store/data/nonogram";


const removeDuplicatesFromArray = (arr : (string | number | [] | undefined)[]) => {
    let withoutDuplicates = arr.filter(
        (element, i) => i === arr.indexOf(element)
    )

    return withoutDuplicates
}

const isValueInRange = (value : number, range : number[]) => {
    const [rangeBegin, rangeEnd] = range
    return value >= rangeBegin && value <= rangeEnd
}


/* use it f.e. to determine longest serie length in array of arrays:
 ([[1, 7, 6, 5], [2, 3, 2, 1, 2], [7, 18, 2]]) will return 5 ([2, 3, 2, 1, 2])
*/
const maximumArrayLengthArrayOfArrays = (arr : Array<Array<number>>) => {
    let [maxLength] = arr.reduce(
        (accumulator, currentValue) => {
            return [
                Math.max(currentValue.length, accumulator[0])
            ];
        }, [0]
    )

    return maxLength
}

const minimumLengthOfNumber = (arr : Array<Array<number>>, num : number) => Math.max(commonFunctions.maximumArrayLengthArrayOfArrays(arr), num)

const minValInArray = (arr : number[]) => Math.min(...arr)
const maxValInArray = (arr : number[]) => Math.max(...arr)

const sumOfArrayNumbers = (numbArr: Array<number>) => {
    return numbArr.reduce(function (previousValue, currentValue) {
        return previousValue + currentValue;
    }, 0);
}

const sequencesNotCorrectWithinDimension = (numbArr: Array<number>, dimension: number) => {
    return sumOfArrayNumbers(numbArr) + (numbArr.length - 1) > dimension 
}

const filterTooLongSequences = (sequencesArr: Array<Array<number>>, dimension: number) => {
    return sequencesArr.filter((sequences) => {
        return sequencesNotCorrectWithinDimension(sequences, dimension)
    })
}

const numbersSumArrayOfArrays = (arr : Array<Array<number>>) => {
    let flatArray : Array<number> = arr.reduce((accumulator, currentValue) => {
        return accumulator.concat(currentValue)
    }, [])

    return sumOfArrayNumbers(flatArray)
}

const checkNonogramCorrectness = (nonogram : selectedNonogramDetails) => {
        
    // data compliance
    if(nonogram.height !== nonogram.rowSequences.length || nonogram.width !== nonogram.columnSequences.length) {
        return -1
    }

    let tooLongRowSequences = commonFunctions.filterTooLongSequences(nonogram.rowSequences, nonogram.width)
    let tooLongColumnSequences = commonFunctions.filterTooLongSequences(nonogram.columnSequences, nonogram.height)

    //any of row/col sequences + gaps are longer than width/height
    if(tooLongColumnSequences.length || tooLongRowSequences.length) {
        return -1
    }

    //sum of numbers in rows === sum of numbers in columns ?
    let sumInRows = commonFunctions.numbersSumArrayOfArrays(nonogram.rowSequences)
    let sumInColumns = commonFunctions.numbersSumArrayOfArrays(nonogram.columnSequences)
        
    if(sumInRows !== sumInColumns) {
        return -1
    }

    return 1
}

function arrayFromIndexRange(startIndex: number, endIndex : number) {
    return Array.apply(0, Array(endIndex - 1))
    .map((element, index) => index + startIndex);
}

const commonFunctions = {
    removeDuplicatesFromArray,
    isValueInRange,
    maximumArrayLengthArrayOfArrays,
    minimumLengthOfNumber,
    minValInArray,
    maxValInArray,
    filterTooLongSequences,
    numbersSumArrayOfArrays,
    arrayFromIndexRange,
    checkNonogramCorrectness
}
export default commonFunctions