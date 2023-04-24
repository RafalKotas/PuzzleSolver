import axios from "axios"
import { sudokuInformation } from "../../store/data/sudoku"

const API_URL = "http://localhost:8080/api/sudoku"

const saveSudokuToFile = (fileName : string, nfd : sudokuInformation) => {
    
    var data = JSON.stringify({
        ...nfd
      });
      
    var saveConfig = {
        method: "post",
        url: API_URL + "/save",
        headers: { 
          "Content-Type": "application/json"
        },
        params: {
            fileName: fileName
        },
        data : data
    };

    return axios(saveConfig)
}

const SudokuService = {
    saveSudokuToFile
}

export default SudokuService