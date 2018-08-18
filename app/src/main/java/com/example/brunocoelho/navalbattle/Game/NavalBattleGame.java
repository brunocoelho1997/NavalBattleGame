package com.example.brunocoelho.navalbattle.Game;

import android.util.Log;

import com.example.brunocoelho.navalbattle.Game.Models.Position;
import com.example.brunocoelho.navalbattle.Game.Models.Profile;
import com.example.brunocoelho.navalbattle.Game.Models.Ships.Ship;
import com.example.brunocoelho.navalbattle.Game.Models.Ships.ShipFive;
import com.example.brunocoelho.navalbattle.Game.Models.Ships.ShipOne;
import com.example.brunocoelho.navalbattle.Game.Models.Ships.ShipThree;
import com.example.brunocoelho.navalbattle.Game.Models.Ships.ShipTwo;
import com.example.brunocoelho.navalbattle.Game.Models.Team;
import com.example.brunocoelho.navalbattle.Profiles.History;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class NavalBattleGame implements Serializable{

    private Data data;


    //temp vars;
    private ArrayList<Position> invalidPositions;
    private ArrayList<Position> firedPositionsTemp;

    private Ship selectedShip;
    private Position onDownPosition = null;
    private Position onMovePosition= null;
    private Position onUpPosition = null;
    private Position lasValidPosition = null;
    private Position initialPositionShip = null; //used when we hitted in 3 positions... isMayChangeShipPosition

    private Profile selectedProfile;


    private boolean mayChangeShipPosition; //if can change...
    //if changed some ship...
    // ...usada para: se tentamos mudar um navio e por validacao voltou para a mesma poicao... ou seja, ainda podemos alterar a sua posicao...
    //e tambem e' usada no online... ou seja, caso seja alguma ship alterada da sua posicao é enviada a equipa para o outro player...
    private boolean changedShipPosition;
    private History history;

    //online
    BufferedReader input;
    PrintWriter output;



    public NavalBattleGame() {
        this.data = new Data();
        invalidPositions = new ArrayList<>();
        this.firedPositionsTemp = new ArrayList<>();
        this.selectedShip = null;
    }

    public Ship getShip(Position position)
    {
        Team team;

        if(isAmITeamA())
            team = getTeamA();
        else
            team = getTeamB();

        for(Ship ship : team.getShips())
        {
            if(ship.getPositionList().contains(position))
                return ship;

        }
        return null;
    }

    public boolean isInsideView(Ship selectedShip) {

        for(Position position : selectedShip.getPositionList()) {
            if (position.getNumber() >= 9 || position.getLetter() >= 9)
                return false;
            if (position.getNumber() <= 0 || position.getLetter() <= 0)
                return false;
        }
        return true;
    }

    public boolean existInvalidPositions()
    {
        return invalidPositions.size()!=0;
    }
    public void refreshInvalidPositions(Ship selectedShip) {

        invalidPositions = new ArrayList<>();

        Team team;

        if(isAmITeamA())
            team = getTeamA();
        else
            team = getTeamB();

        for(Ship ship : team.getShips())
        {

            //colision with another ship
            for(Position position: ship.getPositionList())
            {
                //if exist a ship that constains this position and isnt't the selected one...
                if(selectedShip.getPositionList().contains(position) && !selectedShip.equals(ship))
                    invalidPositions.add(position);

            }

            //adjacent positions
            for(Position position: ship.getAdjacentPositions())
            {
                //if exist a ship that constains this position and isnt't the selected one...
                if(selectedShip.getPositionList().contains(position) && !selectedShip.equals(ship))
                    invalidPositions.add(position);
            }
        }
    }



    public void setAIPositions() {


    }



    private Ship createShip(List<Position> positionList)
    {
        Ship ship = null;

        if(positionList.size() ==1)
            ship = new ShipOne();
        else if(positionList.size() == 2)
            ship = new ShipTwo();
        else if(positionList.size() == 3)
            ship = new ShipThree();
        else if(positionList.size() == 5)
            ship = new ShipFive();


        ship.setPositionList(positionList);
        for(Position position : ship.getPositionList())
        {
            Position p = new Position(position.getNumber(), position.getLetter());
            ship.getInitialPositionList().add(p);
        }

        return ship;
    }

    public void setTeamsPositions() {
        List<Position> positionList = new ArrayList<>();
        List<Ship> teamA = data.getTeamA().getShips();
        List<Ship> teamB = data.getTeamB().getShips();

        Ship ship;

        //2x1
        positionList = new ArrayList<>();
        positionList.add(new Position(1,1));
        teamA.add(createShip(positionList));
        teamB.add(createShip(positionList));


        positionList = new ArrayList<>();
        positionList.add(new Position(1,3));
        teamA.add(createShip(positionList));
        teamB.add(createShip(positionList));

        //2x2
        positionList = new ArrayList<>();
        positionList.add(new Position(1,5));
        positionList.add(new Position(1,6));
        teamA.add(createShip(positionList));
        teamB.add(createShip(positionList));

        positionList = new ArrayList<>();
        positionList.add(new Position(3,2));
        positionList.add(new Position(3,3));
        teamA.add(createShip(positionList));
//        teamB.add(createShip(positionList));

        //2x3
        positionList = new ArrayList<>();
        positionList.add(new Position(3,5));
        positionList.add(new Position(3,6));
        positionList.add(new Position(3,7));
        teamA.add(createShip(positionList));
        teamB.add(createShip(positionList));
        positionList = new ArrayList<>();
        positionList.add(new Position(5,1));
        positionList.add(new Position(5,2));
        positionList.add(new Position(5,3));
        teamA.add(createShip(positionList));
//        teamB.add(createShip(positionList));

        //1x5 T
        positionList = new ArrayList<>();
        positionList.add(new Position(5,5));
        positionList.add(new Position(5,6));
        positionList.add(new Position(5,7));
        positionList.add(new Position(6,6));
        positionList.add(new Position(7,6));
        teamA.add(createShip(positionList));
//        teamB.add(createShip(positionList));
    }

    public void addFiredPosition(Position onUpPosition) {

        //if the position clicked is inside table...
        if (onUpPosition.getNumber() <= 9 && onUpPosition.getLetter() <= 9 && onUpPosition.getLetter()>=1 && onUpPosition.getNumber()>=1)
        {
            //if is my time to play
//            if(isMyTurnToPlay())
//            {
                //verify if the user already not fired to this position
                if(!firedPositionsTemp.contains(onUpPosition) && !getAtualTeam().getFiredPositions().contains(onUpPosition))
                {
                    //just can fire 3 positions...
                    if(firedPositionsTemp.size()<=3)
                    {
                        firedPositionsTemp.add(onUpPosition);
//                        if(isTwoPlayer())
//                            sendPosition(onDownPosition);
                    }
                }
//            }
        }
    }
    public Team getAtualTeam()
    {
        if(data.isTeamATurn())
            return data.getTeamA();
        else
            return data.getTeamB();
    }
    public Team getOppositeTeam()
    {
        if(data.isTeamATurn())
            return data.getTeamB();
        else
            return data.getTeamA();
    }



    public void verifyFiredPosition() {

        Team team = getAtualTeam();


//        TODO: isto para mim nao esta' bem... so esta' a fazer para uma equipa...
//        solucao:
//        Team team;
//
//        //se for um jogo online e nao for a minha vez de jogar vai verificar as firedpositions da equipa contraria...
//        if(isTwoPlayer() && !isMyTurnToPlay())
//            team = getOppositeTeam();
//        else
//            team = getAtualTeam();
//
        int hittedFiredPositions = 0;

        if(firedPositionsTemp.size()==3)
        {
            //add as fired positions...
            team.getFiredPositions().addAll(firedPositionsTemp);

            //if shots hit teamA change icon to cross black.... if not change to cross
            for(Position position : firedPositionsTemp)
            {
                for(Ship ship : getOppositeTeam().getShips()) {
                    if(ship.getPositionList().contains(position))
                    {

                        position.setColor(Constants.BLACK_CROSS_SQUARE);

                        hittedFiredPositions++;
//                        if(isTeamATurn())
//                            Log.d("verifyFiredPosition","TeamA hited in position: " + position);
//                        else
//                            Log.d("verifyFiredPosition","TeamB hited in position: " + position);

                        break;
                    }
                    else
                    {
                        position.setColor(Constants.CROSS_SQUARE);

                    }
                }
            }
        }
        //if hitted in 3 positions and did not yet changed a position of ship... and isnt AI (isnt AI is when not playing two players and is our turn to play - Ignore if AI hit 3 positions) or if we are playing online and is our turn ;
        if(hittedFiredPositions==3 && !isChangedShipPosition() && ( (!isTwoPlayer() && isMyTurnToPlay()) || (isTwoPlayer() && isMyTurnToPlay())))
        {
            mayChangeShipPosition=true;
            Log.d("verifyFiredPosition", "mayChangeShipPosition: " + mayChangeShipPosition);

        }
    }

    public boolean verifyEndOfGame() {

        Team atualTeam = getAtualTeam();

        Team opositeTeam = getOppositeTeam();

        //for all oposite team...
        //percorre a equipa contrária
        for(Ship ship : opositeTeam.getShips())
        {
            //for each ship verify if do not exits any position which that atual team doesnt yet fired.
            //para cada barco vai verificar se existe alguma posicao em que a equipa atual ainda n tenha disparado
            for(Position position : ship.getPositionList())
            {
                if(!atualTeam.getFiredPositions().contains(position))
                {
//                    Log.d("verifyEndOfGame","Found not fired position in another team. Position: " + position);
                    return false;
                }
            }
        }

//        since atual team fired all positions of another team the the game is ended
//        visto que a equipa atual disparou em todos os barcos contrarios esta nahou
        return true;
    }

    public void AIFire() {

        Random rand = new Random();
        int min = 1; int max = 8;

        //while is team B turn to play... since AI is TeamB
        while(firedPositionsTemp.size()!=3)
        {
            firedPositionsTemp.add(new Position(rand.nextInt((max - min) + 1) + min,rand.nextInt((max - min) + 1) + min ));
//            Log.d("AIFire","AI Fired!!!!! Positions:" + firedPositionsTemp.toString());
            verifyFiredPosition();
        }
    }

    public void nextTurn() {
        getFiredPositionsTemp().clear();
        if(isTeamATurn())
            setTeamATurn(false);
        else
            setTeamATurn(true);

        Log.d("nextTurn","isTeamATurn: " + isTeamATurn());
    }

    public boolean isAvaibleNextTurn() {
        return firedPositionsTemp.size()==3;
    }
    public Team getTeamA() {
        if(isTeamATurn())
            return getAtualTeam();
        else
            return getOppositeTeam();
    }

    public Team getTeamB() {
        if(!isTeamATurn())
            return getAtualTeam();
        else
            return getOppositeTeam();
    }

    public boolean isMyTurnToPlay() {
        //if the atual player is temA and is time to teamA play or if the atual player is temB and is time to teamB play return true
        return ((isTeamATurn() && isAmITeamA()) || (!isTeamATurn() && !isAmITeamA()));
    }

    public void setShipsOpositeTeam()
    {

    }
    //    -
//    -
//    -
//    getters and setters
//    -
//    -
//    -
//
    public ArrayList<Position> getFiredPositionsTemp() {
        return firedPositionsTemp;
    }

    public void setFiredPositionsTemp(ArrayList<Position> firedPositionsTemp) {
        this.firedPositionsTemp = firedPositionsTemp;
    }

    public boolean isStarted()
    {
        return data.isStarted();
    }
    public void startGame()
    {
        Log.d("startGame", "O jogo comecou:");
        Log.d("startGame", "isTeamATurn: " + isTeamATurn());
        Log.d("startGame", "isAmITeamA: " + isAmITeamA());

        data.setStarted(true);
    }
    public void endGame()
    {
        data.setStarted(false);
    }
    public ArrayList<Position> getInvalidPositions() {
        return invalidPositions;
    }

    public void setInvalidPositions(ArrayList<Position> invalidPositions) {
        this.invalidPositions = invalidPositions;
    }
    public boolean isTwoPlayer() {
        return data.isTwoPlayer();
    }

    public void setTwoPlayer(boolean twoPlayer) {
        data.setTwoPlayer(twoPlayer);
    }

    public boolean isTeamATurn() {
        return data.isTeamATurn();
    }

    public void setTeamATurn(boolean playerATurn) {
        data.setTeamATurn(playerATurn);
    }

    public boolean isAmITeamA() {
        return data.isAmITeamA();
    }

    public void setAmITeamA(boolean amITeamA) {
        data.setAmITeamA(amITeamA);
    }

    public boolean isMayChangeShipPosition() {
        return mayChangeShipPosition;
    }

    public void setMayChangeShipPosition(boolean mayChangeShipPosition) {
        this.mayChangeShipPosition = mayChangeShipPosition;
    }

    public boolean isChangedShipPosition() {
        return changedShipPosition;
    }

    public void setChangedShipPosition(boolean changedShipPosition) {
        this.changedShipPosition = changedShipPosition;
    }

    public History getHistory() {
        return history;
    }

    public void setHistory(History history) {
        this.history = history;
    }

    public Profile getProfileTeamA()
    {
        return data.getProfileA();
    }
    public Profile getProfileTeamB()
    {
        return data.getProfileB();
    }

    public void setProfileTeamA(Profile profile)
    {
        data.setProfileA(profile);
    }
    public void setProfileTeamB(Profile profile)
    {
        data.setProfileB(profile);
    }

    public Profile getSelectedProfile() {
        return selectedProfile;
    }

    public void setSelectedProfile(Profile selectedProfile) {
        this.selectedProfile = selectedProfile;
    }

    //    -
//    -
//    -
//    clicked positions
//    -
//    -
//    -
//    -
    public void onDown(Position onDownPosition) {

        this.onDownPosition = onDownPosition;


        //if the game already started and we may NOT change position in a ship...
        if(isStarted() && !isMayChangeShipPosition())
        {

        }
        else
        {
            selectedShip = getShip(onDownPosition);

            Log.d("onDown", "\n---" + onDownPosition.toString());

            if(selectedShip!=null)
            {
                Log.d("onDown", selectedShip.toString());

                initialPositionShip = selectedShip.getPointPosition();
                Log.d("onDown", "initialPositionShip:" + initialPositionShip);

            }
        }

    }

    public void onMove(Position onMovePosition) {
        this.onMovePosition = onMovePosition;

        //if the game already started and we may NOT change position in a ship...
        if(isStarted() && !isMayChangeShipPosition())
        {

        }
        else
        {
            if(selectedShip!= null)
            {

                //apenas para a GUI... para quando a ship tiver atingida nao deixar mexer... pq ficava com as posicoes lixadas...
                if(!Collections.disjoint(getOppositeTeam().getFiredPositions(), selectedShip.getPositionList()))
                {
//                    Log.d("ChangeShipPosition", "Can't change ship position because this ship was already hitted by a fire of the other team.");
                    return;
                }

                selectedShip.setPointPosition(onMovePosition);

                //verifica se está dentro do tabuleiro... se nao estiver volta a definir a ship na ultima posicao guardada que era valida...
                //se a nova posicao for valida armazenar como sendo a ultima posicao valida efetuada
                if(isInsideView(selectedShip))
                    lasValidPosition = onMovePosition;
                else
                    selectedShip.setPointPosition(lasValidPosition);

                refreshInvalidPositions(selectedShip);

            }

        }
    }

    //when we receive positions of the other team by online / quando recebemos posicoes novas a partir dos sockets.... isto para evitar q nao sendo a vez dele jogar ele nao pode andar a clicar em posicoes...
    public void onUpOtherTeam(Position onUpPosition)
    {
        if(firedPositionsTemp.size()==3)
            return;

        //if is avaiable next turn we cant click more...
        if(!isAvaibleNextTurn())
            addFiredPosition(onUpPosition);

        verifyFiredPosition();
    }

    public void onUp(Position onUpPosition) {
        this.onUpPosition = onUpPosition;

        //if the game already started and we may NOT change position in a ship and is my turn to play...
        if(isStarted() && !isMayChangeShipPosition() && isMyTurnToPlay())
        {
            //if firedPositionsTemp are already defined ignore new clicks... (estava a adicionar posicoes mesmo após delas todas definidas...)
            if(firedPositionsTemp.size()==3)
                return;

            //if is avaiable next turn we cant click more...
            if(!isAvaibleNextTurn())
                addFiredPosition(onUpPosition);

            verifyFiredPosition();
        }
        else //when selecting position of the ships...
        {
            if(selectedShip!=null)
            {
                verifyRotate();

                selectedShip.setPointPosition(onUpPosition);

//              TODO: understand this again... and comment
                //verifica se está dentro do tabuleiro... se nao estiver volta a definir a ship na ultima posicao guardada que era valida...
                //se a nova posicao for valida armazenar como sendo a ultima posicao valida efetuada
                if(isInsideView(selectedShip))
                    lasValidPosition = onUpPosition;
                else
                    selectedShip.setPointPosition(lasValidPosition);

                refreshInvalidPositions(selectedShip);

//                if current team may change a ship position...
                if(isMayChangeShipPosition())
                {
                    //tried to change position but... if it was invalid put the ship in last position which it was
                    if(verifyIsValidPositionChangeShipPosition())
                    {
                        setMayChangeShipPosition(false);
                        setChangedShipPosition(true);
                    }
                    else
                    {
                        Log.d("onUp", "tried to change position but exists an invalid Positions");

                        selectedShip.setPointPosition(initialPositionShip);
                        refreshInvalidPositions(selectedShip);
                    }
                }
                selectedShip = null;
            }
        }
    }

    boolean verifyIsValidPositionChangeShipPosition() {

//        //if the oposite team already fired to this position...
//        //usa a ship anteriormente criada... mas esta ship auxoliar na posicao inicial
        Ship selectedShipAux = createShip(new ArrayList<Position>(selectedShip.getPositionList()));


        //verifica se ja nao tinha sido disparado para esta nova posicao...
        //Check if one list contains element from the other
        if(!Collections.disjoint(getOppositeTeam().getFiredPositions(), selectedShipAux.getPositionList()))
        {
            Log.d("ChangeShipPosition", "Can't change ship position because the oposite team already fired to this position.");
            return false;

        }

        //if the oposite team already fired to this ship...
        //create a ship with same positions (apenas para ir buscar o numero de posicoes e criar um barco do mesmo tipo...)
        selectedShipAux = createShip(new ArrayList<Position>(selectedShip.getPositionList()));

//        selectedShipAux.setPointPosition(initialPositionShip); TODO: ISTO AINDA ESTA' MAL... se pormos isto sem ser comentarios faz todas as validacoes mas quando escolhemos bem fica lixado...

        //verifica se a outra equipa ja nao tinha disparado numa destas posicoes...
        //Check if one list contains element from the other
        if(!Collections.disjoint(getOppositeTeam().getFiredPositions(), selectedShipAux.getPositionList()))
        {
            Log.d("ChangeShipPosition", "Can't change ship position because this ship was already hitted by a fire of the other team.");
            return false;

        }

        //tried to change position but exists at least an invalid Position... so put the ship in last position which it was
        if(invalidPositions.size()>0)
        {
            Log.d("ChangeShipPosition", "Can't change ship position because you moved to an invalid position.");
            return false;

        }

        //if inst adjancet return false
        if(!initialPositionShip.isAdjacent(onUpPosition))
        {
            Log.d("ChangeShipPosition", "Can't change ship position because you moved more than 1 position.");
            return false;

        }

        return true;
    }

    private void verifyRotate() {
        //if down, move and up same position so this is a rotate
        if(onDownPosition.equals(onUpPosition))
        {
            //if is to change ship position after 3 hitted fired positions return... dont allow rotate
            if(isMayChangeShipPosition())
                return;

            selectedShip.rotate();
            //rotate while doesn't find valid rotation
            while(true)
            {
                selectedShip.setPointPosition(onUpPosition);
                if(!isInsideView(selectedShip))
                    selectedShip.rotate();
                else
                    break;
            }
        }
    }




    //    -
//    -
//    -
//    online methods
//    -
//    -
//    -
//


    public void setOppositeTeam(Team team)
    {
        if(data.isAmITeamA())
            data.setTeamB(team);
        else
            data.setTeamA(team);
    }

    public BufferedReader getInput() {
        return input;
    }

    public void setInput(BufferedReader input) {
        this.input = input;
    }

    public PrintWriter getOutput() {
        return output;
    }

    public void setOutput(PrintWriter output) {
        this.output = output;
    }


    public void defineShipsType(Team oppositeTeam) {
        List<Ship> shipTypeDefinedList = new ArrayList<>();

        for(Ship ship: oppositeTeam.getShips())
        {
            Ship newShip = createShip(ship.getPositionList());
            newShip.setRotation(ship.getRotation());
            newShip.setInitialPositionList(new ArrayList<Position>(ship.getInitialPositionList()));
            shipTypeDefinedList.add(newShip);
        }

        oppositeTeam.setShips(shipTypeDefinedList);
    }

    public void restoreData() {
        boolean isTwoPlayer = data.isTwoPlayer();
        this.data = new Data();
        data.setTwoPlayer(isTwoPlayer); //TODO: isto nao esta' mt bem
    }

    public Profile generateAIProfile()
    {
        return new Profile(Constants.NAME_PROFILE_AI);
    }

    public void setProfile(Profile profile) {
        if(isAmITeamA())
        {
            setProfileTeamB(profile);
            history.setProfileTeamB(profile);
        }
        else
        {
            setProfileTeamA(profile);
            history.setProfileTeamA(profile);

        }

    }
}
