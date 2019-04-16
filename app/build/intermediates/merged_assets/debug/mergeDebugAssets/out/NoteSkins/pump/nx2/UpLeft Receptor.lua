local player = Var "Player" or GAMESTATE:GetMasterPlayerNumber()

local function Beat(self)
	-- too many locals
	local this = self:GetChildren()
	local playerstate = GAMESTATE:GetPlayerState( player )
	local songposition = playerstate:GetSongPosition() -- GAMESTATE:GetSongPosition()
	
	local beat = songposition:GetSongBeat() -- GAMESTATE:GetSongBeat()
	
	local part = beat%1
	part = clamp(part,0,0.5)
	local eff = scale(part,0,0.5,1,0)
	if (songposition:GetDelay() or false) and part == 0 then eff = 0 end
	if beat < 0 then
		eff = 0
	end
	this.Glow:diffusealpha(eff);
	
	--ojo acá con el FREEDOM
	if playerstate:GetPlayerOptions('ModsLevel_Preferred' ):Dark() == 1 then
		this.Glow:diffusealpha(0);
	end;
end

return Def.ActorFrame {
	-- COMMANDS --
	InitCommand=cmd(SetUpdateFunction,Beat);
	-- LAYERS --
	-- NOTESKIN:LoadActor("Center", "Outline Receptor")..{
		-- Name="Outline Full";
		-- Condition=Var "Button" == "Center" and GAMESTATE:GetCurrentStyle():GetStepsType() ~= 'StepsType_Pump_Halfdouble';
		-- InitCommand=cmd(x,96);
	-- };
	-- NOTESKIN:LoadActor("DownLeft", "Outline Receptor")..{
		-- Name="Outline Half";
		-- Condition=Var "Button" == "DownLeft" and GAMESTATE:GetCurrentStyle():GetStepsType() == 'StepsType_Pump_Halfdouble';
		-- InitCommand=cmd(x,96);
	-- };
	
	NOTESKIN:LoadActor(Var "Button", "Ready Receptor")..{
		Name="Base";
		Frames={
			{ Frame = 0, Delay = 0 }
		};
		
		--hans un truco de magia
		OnCommand=function(self)
			local mods = GAMESTATE:GetPlayerState( player ):GetPlayerOptionsArray( 'ModsLevel_Preferred' );
			for i=1,#mods,1 do
				if string.lower(mods[i])=="dark" then
					self:visible(false);
				end;
			end;
		end;
	};
	NOTESKIN:LoadActor(Var "Button", "Ready Receptor")..{
		Name="Glow";
		Frames= {
			{ Frame = 1, Delay = 0 }
		};
		InitCommand=cmd(blend,'BlendMode_Add');
		PressCommand=cmd(finishtweening;linear,0.05;zoom,0.9;linear,0.1;zoom,1);
	};
	
	NOTESKIN:LoadActor(Var "Button", "Ready Receptor")..{
		Name="PumpGlow";
		Frames = { { Frame = 2 } };
		InitCommand=cmd(diffusealpha,0);
		PressCommand=cmd(finishtweening;diffusealpha,1;zoom,1;linear,0.2;zoom,1.2;diffusealpha,0);
	};
	--[[
	NOTESKIN:LoadActor(Var "Button", "Ready Receptor")..{
		Name="Tap";
		Frames = { Frame = 2 };
		InitCommand=cmd(zoom,1;diffusealpha,0;glow,1,1,1,0);
		--NOTESKIN:GetMetricA(Var "Button", "TapInitCommand");
		--
		PressCommand=cmd(finishtweening;glow,1,1,1,1;zoom,1;linear,0.2;glow,1,1,1,0;zoom,1.2);
		--NOTESKIN:GetMetricA(Var "Button", "TapHeldCommand");
		--
	};
	--]]
}