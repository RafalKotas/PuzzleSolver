export const findLastIndexContaining = (array : string[], matchingSubstring : string) => {
    return array.reduce((prevValue, currentValue, index) => {
        return currentValue.includes(matchingSubstring) ? index : prevValue
    }, -1)
}

export const arrayReversedString = (array : string[]) => {
    return array.reduce((prevValue, currentValue) => {
        return [currentValue].concat(prevValue)
    }, [] as string[])
}

export const arrayReversedNumber = (array : number[]) => {
    return array.reduce((prevValue, currentValue) => {
        return [currentValue].concat(prevValue)
    }, [] as number[])
}

export const createArrayOfEmptyFields = (arrLength: number) => Array.from({length: arrLength}, () => { return "-" })

export const generateArrayOfSequenceMarks = (marks : number) => 
    Array.from({ length: marks }, (_, i) => String.fromCharCode("a".charCodeAt(0) + i));