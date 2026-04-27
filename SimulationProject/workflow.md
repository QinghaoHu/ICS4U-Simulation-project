# Project Workflow

This file preserves the old README note and organizes the original planning draft into a workflow document.

## Previous README Note

This will be completed after every group work is finished.

## Original Request

"Can someone make a tab that explains everything, like what each unit does and whatnot?"

The new `README.md` is intended to answer that request for players and reviewers. This workflow file keeps the team assignments, calendar, and todo list.

## People Tasks

### Fred

Primary focus: soldiers, bullets, and attack behavior.

Tasks:

- Implement the bullet class into the soldier class so marines can shoot every class on the opposite team, including buildings, workers, and soldiers.
- Make soldiers immediately move toward an enemy unit or base after spawning from barracks.
- Add a maximum enemy range so units cannot kill enemies from across the map.
- Make sure enemies take time to kill.
- If enemies die too fast, add attack delay, lower damage, or increase unit health.
- If finished early, work on the barracks class so it spawns soldiers beside the building when `ATK` is chosen.

### Yueheng

Primary focus: turrets and defensive projectiles.

Tasks:

- Finish the turret class.
- Make turrets shoot only close enemies.
- Implement projectile behavior for turrets.
- Make turrets attack only enemy soldiers if that is the intended balance.
- Differentiate spawning for Red Team and Blue Team.
- Make turrets add to the world when `DEF` is chosen.
- Keep turret range short so it cannot snipe across the map.
- If finished early, start working on the pickup bot.
- Pickup bot should spawn from a home base and wait until a package is spawned.

### Qinghao

Primary focus: team strategy logic.

Tasks:

- Work on `ECO`, `ATK`, and `DEF`.
- `ECO`: build 5 workers.
- `ATK`: rush barracks and make 1 soldier.
- `DEF`: build a defense turret and make 1 worker.
- After a strategy is complete, reroll for the next strategy.
- Add a 7 second cooldown to build workers.
- Add a 10 second cooldown to build marines.
- If a barracks already exists and `ATK` is chosen again, build 4 soldiers and 1 worker instead.
- If finished early, work on `REG`, where the team builds 3 workers and a package-drop pickup bot.

### Anna

Primary focus: supply drops and rewards.

Tasks:

- Finish the supply drop class.
- Implement upgrades over roughly two work periods.
- Common reward chance: 70 percent.
- Common rewards: 75 resource package drop or +1 worker package drop.
- Rare reward chance: 25 percent.
- Rare rewards: 150 resource package drop or +2 worker package drop.
- Ultra rare reward chance: 5 percent.
- Ultra rare rewards: 250 resource package drop, +3 worker package drop, or +1 marine package drop if barracks has been built.
- Add package drops randomly through the main world.
- Suggested timing: one package every 5 to 10 seconds.
- Consider a designated bot whose only purpose is picking up supply drops for a team.
- Make package drops spawn near the middle so both teams have a fair chance.

### Sam

Primary focus: resource economy and building costs.

Planned costs:

- Worker: 50 minerals.
- Marine: 75 minerals.
- Defense turret: 175 minerals.
- Barracks: 150 minerals.

Tasks:

- Deal with resource increase and subtraction rate.
- Implement worker building behavior for barracks when `ATK` is chosen.
- Barracks should be built near the base by a worker.
- Suggested barracks build time: 10 seconds.
- Worker should return to mining after construction.
- Implement worker building behavior for turrets when `DEF` is chosen.
- Turrets should be built defensively near the home base.

### Graphics / UI Owner

Primary focus: graphics, UI, setup screen, and possible extra features.

Tasks:

- Create and upload supply drop graphics.
- Create extra middle mineral resource graphics.
- Create improved backdrop graphics.
- Create worker-building item graphics.
- Implement UI graphics so the UI displays chosen strategies.
- Create package pickup bot.
- Create upgrade graphics for each upgrade.
- Implement upgrade graphics so the game shows which upgrades were picked up.
- Coordinate with Anna once supply drops are complete.

If finished:

- Create a pickup bot that costs 80 minerals.
- The pickup bot only collects package drops and can be killed.
- Add the bot to `REG`, where the team can choose 2 workers and 1 pickup bot.
- Consider adding either a melee unit or a sniper.
- Melee unit idea: high health tank, very low damage.
- Sniper idea: medium damage, very low health, can be one-shot.
- Balance damage and health near the end of the project.

Possible extra supply-drop effects:

- Kill the package pickup bot.
- Give the opponent an extra worker.
- Give the opponent 50 minerals.

Possible home base upgrades:

- Build workers faster, from 7 seconds to 4 seconds.
- Lower worker cost to 45 minerals.
- Build marines faster, from 10 seconds to 7 seconds.
- Lower marine cost to 70 minerals.
- Upgrade cost idea: 300 minerals.

Setup screen tasks:

- Start working on the pre-simulation option screen.
- Ask for user input so they can choose initial team strategies.
- Let users choose whether to include supply drops.

If everything is finished:

- Create a chaos mode with ridiculous upgrades.
- 2x mineral collection rate.
- No cooldowns between unit spawning.
- 500 mineral package drops.
- All units gain 500 HP.

## Calendar

### 4/23/2026

Tasks:

- Person 1: Turret should only spawn in the middle.
- Person 2: Resources need to be spendable.
- Person 3: Begin constructing the starting world. This was not done.
- Person 4: Begin working on the UI for the starting world: `ECO`, `ATK`, `DEF`. This was not done.
- Person 5: Work on the game-over screen that shows which team won.
- Person 6: Work on the push-away method so marines do not get stuck behind each other.

### 4/24/2026

Tasks:

- Person 1: Create a fourth option, `REG`, that implements the resource package bot.
- Person 2: Begin working on balance changes.
- Person 3: Begin constructing the starting world.
- Person 4: Start constructing UI for the starting world.
- Person 5: Balance patch.
- Person 6: Finalize ending world.

### Weekend

Tasks:

- Finish UI graphics so they can be implemented for better viewing.
- Begin checking for bugs.
- Balance gameplay so the simulation does not take too long or too short.
- Add sound.

### Monday

Tasks:

- Person 1: Finalize starting world.
- Person 2: Finalize starting world UI.
- Person 3: Last-minute strategy and balance check.
- Person 4: Bug check.
- Person 5: Bug check.
- Person 6: Add sound.

## Current Documentation Split

- `README.md`: English project explanation for players, reviewers, and teammates.
- `workflow.md`: planning notes, todo list, calendar, and people tasks.
