import React, {Component, useCallback} from "react";
import { Button, TextField } from '@material-ui/core';
import axios from 'axios';
const axiosPOSTconfig = {headers: {'Content-Type': 'application/json'}};


class CreateKey extends Component{

    constructor(props) {
        super(props)
        this.state = {
            key: "",
            game: ""
        }
    }

    onChange = (e) => {
        this.setState({[e.target.id]: e.target.value});
    }

    onSubmit = (event) => {
        let {key, game} = this.state;
        event.preventDefault();
        axios.post('http://localhost:8082/keys/create', JSON.stringify({
            'key': key,
            'game': game
        }), axiosPOSTconfig)
            .then((response) => {
                this.setState({status: response.data.status});
            })
            .catch((error) => {console.log(error)});
    }

    render() {
        let {key, game} = this.state;
        return(
            <main>
                <div>
                    <form onSubmit={this.onSubmit}>
                        <TextField id="key" type="text" value={key} placeholder={"Key"} onChange={this.onChange}/><br/>
                        <TextField id="game" type="text" value={game} placeholder={"Game"} onChange={this.onChange}/><br/>
                        <Button onClick={this.onSubmit} variant="contained" color="primary">Create Game</Button>
                    </form>
                </div>
            </main>
        );
    }
}
export default CreateKey;
