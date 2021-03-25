import './App.css';
import React, {useEffect, useState, useCallback} from "react";
import axios from "axios";
import {useDropzone} from 'react-dropzone'

const Game = () => {

    const [game, setGame] = useState([]);

    const fetchGames = () => {
        axios.get("http://localhost:8082/games").then(res => {
            console.log(res);
            setGame(res.data.data);
        })
    }

    useEffect(() => {
        fetchGames();
    }, []);

    return game.map((game, index) => {
            return (
                <div key={index}>
                    {game.id ? (
                        <img src={'http://localhost:8082/games/${id}/mainImage'}/>) : null}
                    <h1>{game.name}</h1>
                    <p>{game.price}</p>
                    <MyDropzone/>
                </div>
            );
        }
    );
};

function MyDropzone() {
    const onDrop = useCallback(acceptedFiles => {
        const file = acceptedFiles[0];
        console.log(file);
        const formData = new FormData();
        formData.append("file", file);
        // axios.post()
        // Do somethin  g with the files
    }, [])
    const {getRootProps, getInputProps, isDragActive} = useDropzone({onDrop})

    return (
        <div {...getRootProps()}>
            <input {...getInputProps()} />
            {
                isDragActive ?
                    <p>Drop the files here ...</p> :
                    <p>Drag 'n' drop some files here, or click to select files</p>
            }
        </div>
    )
}

function App() {
    return (
        <div className="App">
            <Game/>
        </div>
    );
}

export default App;
