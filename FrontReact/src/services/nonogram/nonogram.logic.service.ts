import axios from "axios"
import { nonogramRelatedLogicData } from "../../store/puzzleLogic/nonogram"

const API_URL = "http://localhost:5000/api/nonogram/logic"

const initializeSolverData = (nonogramRelatedData: nonogramRelatedLogicData) => {
      
    var data = JSON.stringify({
        ...nonogramRelatedData
    })

    var initConfig = {
        method: "post",
        url: API_URL + "/init",
        headers: { 
          "Content-Type": "application/json"
        },
        params: {

        },
        data: data
    }

    return axios(initConfig)
}

const colourFieldsInColumnsRange = (nonogramRelatedData: nonogramRelatedLogicData, columnBegin: number, columnEnd: number) => {
    var data = JSON.stringify({
        ...nonogramRelatedData
    })

    var config = {
        method: "post",
        url: API_URL + `/fillOverlappingColumnsSequences/${columnBegin}/${columnEnd}`,
        headers: { 
          "Content-Type": "application/json"
        },
        params: {
            columnBegin,
            columnEnd
        },
        data: data
    }

    return axios(config)
}

const colourFieldsInRowsRange = (nonogramRelatedData: nonogramRelatedLogicData, rowBegin: number, rowEnd: number) => {
    var data = JSON.stringify({
        ...nonogramRelatedData
    })

    var config = {
        method: "post",
        url: API_URL + `/fillOverlappingRowsSequences/${rowBegin}/${rowEnd}`,
        headers: { 
          "Content-Type": "application/json"
        },
        /*params: {
            rowBegin,
            rowEnd
        },*/
        data: data
    }

    return axios(config) 
}

const markFieldsInRowsRange  = (nonogramRelatedData: nonogramRelatedLogicData, rowBegin: number, rowEnd: number) => {
    var data = JSON.stringify({
        ...nonogramRelatedData
    })

    var config = {
        method: "post",
        url: API_URL + `/markRowsSequences/${rowBegin}/${rowEnd}`,
        headers: { 
          "Content-Type": "application/json"
        },
        params: {
            rowBegin,
            rowEnd
        },
        data: data
    }

    return axios(config) 
}

const markFieldsInColumnsRange  = (nonogramRelatedData: nonogramRelatedLogicData, columnBegin: number, columnEnd: number) => {
    var data = JSON.stringify({
        ...nonogramRelatedData
    })

    var config = {
        method: "post",
        url: API_URL + `/markColumnsSequences/${columnBegin}/${columnEnd}`,
        headers: { 
          "Content-Type": "application/json"
        },
        params: {
            columnBegin,
            columnEnd
        },
        data: data
    }

    return axios(config) 
}

const placeXinRowsRange  = (nonogramRelatedData: nonogramRelatedLogicData, rowBegin: number, rowEnd: number) => {
    var data = JSON.stringify({
        ...nonogramRelatedData
    })

    var config = {
        method: "post",
        url: API_URL + `/placeXinRowsRange/${rowBegin}/${rowEnd}`,
        headers: { 
          "Content-Type": "application/json"
        },
        params: {
            rowBegin,
            rowEnd
        },
        data: data
    }

    return axios(config) 
}

const placeXinColumnsRange  = (nonogramRelatedData: nonogramRelatedLogicData, columnBegin: number, columnEnd: number) => {
    var data = JSON.stringify({
        ...nonogramRelatedData
    })

    var config = {
        method: "post",
        url: API_URL + `/placeXinColumnsRange/${columnBegin}/${columnEnd}`,
        headers: { 
          "Content-Type": "application/json"
        },
        params: {
            columnBegin,
            columnEnd
        },
        data: data
    }

    return axios(config) 
}

const testCustomSolution = (nonogramRelatedData: nonogramRelatedLogicData, solutionFileName: string) => {
    var data = JSON.stringify({
        ...nonogramRelatedData
    })

    var config = {
        method: "post",
        url: API_URL + `/customSolutionPart`,
        headers: { 
          "Content-Type": "application/json"
        },
        params: {
            solutionFileName
        },
        data: data
    }

    return axios(config) 
}

const saveSolution = (nonogramRelatedData: nonogramRelatedLogicData, fileName: string) => {
    var data = JSON.stringify({
        ...nonogramRelatedData
    })

    var config = {
        method: "post",
        url: API_URL + `/saveSolution`,
        headers: { 
          "Content-Type": "application/json"
        },
        params: {
            fileName
        },
        data: data
    }

    return axios(config) 
}

const compareWithSolution = (nonogramRelatedData: nonogramRelatedLogicData, fileName: string) => {
    var data = JSON.stringify({
        ...nonogramRelatedData
    })

    var config = {
        method: "post",
        url: API_URL + `/compareWithSolution`,
        headers: { 
          "Content-Type": "application/json"
        },
        params: {
            fileName
        },
        data: data
    }

    return axios(config) 
}

const correctRanges = (nonogramRelatedData: nonogramRelatedLogicData) => {
    var data = JSON.stringify({
        ...nonogramRelatedData
    })

    var config = {
        method: "post",
        url: API_URL + `/correctRanges`,
        headers: { 
          "Content-Type": "application/json"
        },
        data: data
    }

    return axios(config) 
}

const correctColumnsRanges  = (nonogramRelatedData: nonogramRelatedLogicData, columnBegin: number, columnEnd: number) => {
    var data = JSON.stringify({
        ...nonogramRelatedData
    })

    var config = {
        method: "post",
        url: API_URL + `/correctColumnsRanges/${columnBegin}/${columnEnd}`,
        headers: { 
          "Content-Type": "application/json"
        },
        params: {
            columnBegin,
            columnEnd
        },
        data: data
    }

    return axios(config) 
}

const correctRowsRanges  = (nonogramRelatedData: nonogramRelatedLogicData, rowBegin: number, rowEnd: number) => {
    var data = JSON.stringify({
        ...nonogramRelatedData
    })

    var config = {
        method: "post",
        url: API_URL + `/correctRowsRanges/${rowBegin}/${rowEnd}`,
        headers: { 
          "Content-Type": "application/json"
        },
        params: {
            rowBegin,
            rowEnd
        },
        data: data
    }

    return axios(config) 
}

const NonogramLogicService = {
    initializeSolverData,
    colourFieldsInColumnsRange,
    colourFieldsInRowsRange,
    markFieldsInRowsRange,
    markFieldsInColumnsRange,
    placeXinRowsRange,
    placeXinColumnsRange,
    testCustomSolution,
    saveSolution,
    compareWithSolution,
    correctRanges,
    correctRowsRanges,
    correctColumnsRanges
}

export default NonogramLogicService