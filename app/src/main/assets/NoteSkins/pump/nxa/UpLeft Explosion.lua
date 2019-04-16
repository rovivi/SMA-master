local iState = 0;
return Def.ActorFrame {
	--note graphic
	NOTESKIN:LoadActor(Var "Button", "Tap Note") .. {
		InitCommand=cmd(blend,"BlendMode_Add";playcommand,"Glow");
		W1Command=cmd(playcommand,"Glow");
		W2Command=cmd(playcommand,"Glow");
		W3Command=cmd(playcommand,"Glow");
		W4Command=cmd();
		W5Command=cmd();
		
		HitMineCommand=cmd(playcommand,"Glow");
		GlowCommand=cmd(setstate,0;finishtweening;diffusealpha,1.0;zoom,1.0;linear,0.15;diffusealpha,0.9;zoom,1.15;linear,0.15;diffusealpha,0.0;zoom,1.3);
		HeldCommand=cmd(playcommand,"Glow");
	};
	--tap
	NOTESKIN:LoadActor(Var "Button", "Ready Receptor")..{
		Name="Tap";
		--Frames = { { Frame = 2 ; Delay = 1 } };
		TapCommand=cmd(finishtweening;diffusealpha,1;zoom,1;linear,0.2;diffusealpha,0;zoom,1.2);
		InitCommand=cmd(pause;setstate,2;playcommand,"Tap");
		HeldCommand=cmd(playcommand,"Tap");
		ColumnJudgmentMessageCommand=cmd(playcommand,"Tap");
		--TapNoneCommand=cmd(playcommand,"Tap");
	};
	--explosion
	LoadActor("_explosion 6x1")..{
		InitCommand=cmd(blend,"BlendMode_Add";pause;playcommand,"Frames");
		W1Command=function(self) self:finishtweening(); iState = 0 self:playcommand("Frames") end;
		W2Command=function(self) self:finishtweening(); iState = 0 self:playcommand("Frames") end;
		W3Command=function(self) self:finishtweening(); iState = 0 self:playcommand("Frames") end;
		W4Command=cmd();
		W5Command=cmd();
		HoldingOnCommand=function(self) self:finishtweening(); iState = 0 self:playcommand("Frames") end;
		HitMineCommand=function(self) self:finishtweening(); iState = 0 self:playcommand("Frames") end;
		HeldCommand=function(self) self:finishtweening(); iState = 0 self:playcommand("Frames") end;
		ItemCommand=function(self) self:finishtweening(); iState = 0 self:playcommand("Frames") end;
		
		FramesCommand=function(self)			
			self:setstate(iState);
			self:diffusealpha(1);
			self:linear(0.06);
			iState = iState+1
			if iState < self:GetNumStates() then
				self:queuecommand("Frames");
			end
		end;
	};
	--thing...
	Def.Quad {
		InitCommand=cmd(zoomto,50,5000;diffusealpha,0);
		HitMineCommand=cmd(finishtweening;diffusealpha,1;linear,0.3;diffusealpha,0);
	};
}