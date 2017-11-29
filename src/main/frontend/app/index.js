import React from 'react';
import ReactDOM from 'react-dom';
import { Router, Route, Link, IndexRoute } from 'react-router';
import $ from 'jquery';
import '../style/style.css';

class NavBar extends React.Component {
	constructor (props) {
		super(props);
		this.state = {};
	}
	
	render() {
		return (
			<nav className="navbar navbar-default">
				<div className="container-fluid">
					<div className="navbar-header">
						<Link className="navbar-brand" to="/ranks">Foos Rank</Link>
					</div>
					<ul className="nav navbar-nav">
						<li><Link to="/ranks">Ranks</Link></li>
						<li><Link to="/game">Game</Link></li>
						<li><Link to="/new">Add</Link></li>
					</ul>
				</div>
			</nav>
		);
	}
}

class App extends React.Component {
	render() {
		return (
			<div>
				<NavBar />
				{this.props.children}
			</div>
		);
	}	
}

class Player extends React.Component {
	constructor (props) {
		super(props);
		this.state = {
			username: this.props.player.username,
			elo: this.props.player.elo,
			rank: this.props.index,
			wins: this.props.player.wins,
			losses: this.props.player.losses
		};
	}
	
	render() {
		return (
			<tr>
				<td>{this.state.rank}</td>
				<td>{this.state.username}</td>
				<td>{this.state.elo.toFixed(0)}</td>
				<td>{this.state.wins}/{this.state.losses}</td>
			</tr>
		);
	}
}

class PlayerTable extends React.Component {
	constructor (props) {
		super(props);
		var _this = this;
		_this.state = {playerRows: []};
		$.get("/api/players", function(data, status) {
			if(status === 'success')
			{
				var rows = [];
				var playersList = data;
				if(playersList){
					playersList.sort(function(a, b) {return b.elo - a.elo;});
					var playerIndex = 0;
					playersList.forEach(function(player){
						rows.push(<Player index={++playerIndex} player={player} key={player.id} />);
					});
				}
				_this.setState({playerRows: rows});
			}
			else {
				window.location.replace('/web-error');
			}
		});
	}
	
	render() {
		return(
			<table className="table table-striped">
				<thead>
					<tr>
						<th>Rank</th>
						<th>Name</th>
						<th>Elo</th>
						<th>W/L</th>
					</tr>
				</thead>
				<tbody>
					{this.state.playerRows}
				</tbody>
			</table>
		);
	}
}

class NewPlayer extends React.Component {
	constructor(props) {
		super(props);
		this.state = {name: ''};
		
		this.handleNameChange = this.handleNameChange.bind(this);
		this.handleSubmit = this.handleSubmit.bind(this);
	}
	
	handleNameChange(event) {
		this.setState({name: event.target.value});
	}
	
	handleSubmit(event) {
		$.ajax({
			type: 'POST',
			url: '/api/players',
			data: this.state.name,
			success: function(data){
				window.location.replace('/');
			},
			error: function() {
				window.location.replace('/web-error');
			},
			contentType: "text/plain"
		});
	}
	
	render() {
		return (
			<form onSubmit={this.handleSubmit}>
				<div className="form-group">
					<label>
						Name:
						<input className="form-control" type="text" name="name" value={this.state.name} onChange={this.handleNameChange} />
					</label>
				</div>
				<input className="btn btn-default" type="submit" value="Submit" />
			</form>
		);
	}
}

class Error extends React.Component {
	constructor(props) {
		super(props);
	}
	
	render() {
		return (
			<div className="container">
				<div className="jumbotron">
					<h1>Error</h1>
					<p>Something went wrong! :(</p>
				</div>
			</div>
		);
	}
}

class Game extends React.Component {
	constructor(props) {
		super(props);
		var _this = this;
		_this.state = {playerA1: -1, playerA2: -1, playerB1: -1, playerB2: -1, didAWin: null, playerList: []};
		
		_this.handlePlayerChange = _this.handlePlayerChange.bind(_this);
		_this.handleSubmit = _this.handleSubmit.bind(this);
		
		$.get("/api/players", function(data, status) {
			if(status === 'success')
			{
				var rows = [];
				rows.push(<option value="-1" key="-1">-Select-</option>);
				var playersList = data;
				if(playersList){
					playersList.sort(function(a, b) {
						if(a.username < b.username) {
							return -1;
						}
						if(a.username > b.username) {
							return 1;
						}
						return 0;
					});
					playersList.forEach(function(player){
						rows.push(<option value={player.id} key={player.id}>{player.username}</option>);
					});
				}
				_this.setState({playerList: rows});
			}
			else {
				window.location.replace('/web-error');
			}
		});
	}
	
	handlePlayerChange(event) {
		var target = event.target;
		var name = target.name;
		var value = target.value;
		this.setState({[name]: value});
	}
	
	handleSubmit(event) {
		$.ajax({
			url: '/api/games',
			type: 'PUT',
			contentType: 'application/json',
			data: JSON.stringify({teamA: [this.state.playerA1, this.state.playerA2], teamB: [this.state.playerB1, this.state.playerB2], didAWin: this.state.didAWin}), 
			success: function(data){
				window.location.replace('/');
			},
			error: function() {
				window.location.replace('/web-error');
			}
		});
	}
	
	render() {
		const { playerA1, playerA2, playerB1, playerB2, didAWin} = this.state;
		var isEnabled = this.state.playerA1 != -1 && this.state.playerA2 != -1 && this.state.playerB1 != -1 && this.state.playerB2 != -1 && this.state.didAWin != null;
		return (
			<form onSubmit={this.handleSubmit}>
				<div className="form-group">
					<label>
						<input type="radio" name="didAWin" value={true} onChange={this.handlePlayerChange} />Team A:
						<select className="form-control" onChange={this.handlePlayerChange} value={this.playerA1} name="playerA1">
							{this.state.playerList}
						</select>
						<select className="form-control" onChange={this.handlePlayerChange} value={this.playerA2} name="playerA2">
							{this.state.playerList}
						</select>
					</label>
				</div>
				<div className="form-group">
					VS.
				</div>
				<div className="form-group">
					<label>
						<input type="radio" name="didAWin" value={false} onChange={this.handlePlayerChange} />Team B:
						<select className="form-control" onChange={this.handlePlayerChange} value={this.playerB1} name="playerB1">
							{this.state.playerList}
						</select>
						<select className="form-control" onChange={this.handlePlayerChange} value={this.playerB2} name="playerB2">
							{this.state.playerList}
						</select>
					</label>
				</div>
				<input className="btn btn-default" type="submit" value="Submit" disabled={!isEnabled} />
			</form>
		);
	}
}

ReactDOM.render(
	<Router>
		<Route path="/" component={App}>
			<IndexRoute component={PlayerTable} />
			<Route path="ranks" component={PlayerTable} />
			<Route path="game" component={Game} />
			<Route path="new" component={NewPlayer} />
			<Route path="web-error" component={Error} />
		</Route>
		<Route path="web-error" component={Error} />
	</Router>,
	document.querySelector('.container'));