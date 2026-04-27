# Siege Simulation

Siege Simulation is a Greenfoot Java RTS-style battle simulation. Two AI-controlled teams, Red and Blue, gather resources, build units and structures, choose strategies, collect supply drops, and try to destroy the enemy base.

This README explains the current implementation: what each unit does, what each building does, how strategies work, and what the main support classes are responsible for.

Planning notes, original team assignments, and the work calendar are stored in `workflow.md`.

## How To Run

1. Open the project folder in Greenfoot.
2. Compile the scenario.
3. Run `StartingWorld`, then use the start/config flow to enter the simulation.
4. The main battle runs in `MyWorld`.

## Main Simulation Loop

`MyWorld` is the main battle world.

It currently:

- Creates Red Team and Blue Team.
- Places both bases.
- Places three resource nodes near each team.
- Adds the UI.
- Adds both `Team` actors to the world so their `act()` methods run.
- Applies optional pre-simulation setup from `SimulationConfig`.
- Spawns supply drops if supply drops are enabled.
- Shows an FPS counter at the top of the world for debugging.

World size:

- Width: `1200`
- Height: `800`

Supply drops:

- Spawn between x `500` and x `700`.
- Spawn above the world and fall into the play area.
- Have a maximum of 5 active drops at once.
- Respawn after a randomized delay.

## Teams

`Team` is the AI controller for one side.

Each team stores:

- Team id: Red or Blue.
- Team name.
- Current resources.
- Units.
- Workers.
- Supply bots.
- Buildings.
- Barracks.
- Turrets.
- Current strategy.
- Base reference.
- Build/production cooldowns.

Each team actor runs its own `act()` method. During that method, it:

- Cleans dead or removed workers and supply bots.
- Tracks current worker, barrack, turret, marine, and supply bot counts.
- Spawns workers if the strategy requires more workers.
- Assigns workers to build barracks.
- Uses barracks to spawn marines.
- Spawns supply bots when needed.
- Assigns workers to build turrets.
- Rerolls strategy after the current goal is complete.
- Upgrades the base when enough resources are available.
- Forces a strategy reset if the team appears stuck.

## Strategies

The strategy system is implemented in `Team`.

### ECO

Economy strategy.

Current behavior:

- Initial setup asks for workers.
- Later ECO rerolls add more workers.
- The goal is to increase mining income.

### ATK

Attack strategy.

Current behavior:

- Builds toward barracks.
- Produces marines once barracks exist.
- Adds more barracks until the maximum is reached.
- Uses marines as the main attacking unit.

### DEF

Defense strategy.

Current behavior:

- Builds defensive turrets.
- Uses workers to place turrets near the team's defensive side.
- Turrets attack enemies in range.

### REG

Regular / utility strategy.

Current behavior:

- Uses supply bots.
- Focuses on collecting supply drops instead of only building economy, attack, or defense.

## Units

### Worker

Class: `Worker`

Worker is the economy and construction unit.

Current behavior:

- Spawns from the team's base.
- Is registered with its team.
- Gets assigned to one of the team's resource nodes.
- Moves to the resource node.
- Mines after a timer.
- Carries resources.
- Returns to the home base.
- Deposits resources into the team's resource pool.
- Repeats the mining/deposit cycle.
- Can receive building jobs for barracks or turrets.
- Moves to the build location.
- Waits through a longer build timer.
- Adds the finished building to the world.
- Returns to mining after building.

Important implementation details:

- Workers use a queue of states such as `move`, `mining`, `depositing`, and `building`.
- Workers use a queue of target positions.
- Workers use a queue of buildings to construct.
- Red and Blue workers rotate through their own resource nodes using static indexes.
- Worker images switch between regular and mining versions based on whether they are carrying resources.

### Marine

Class: `Marine`

Marine is the main combat soldier.

Current behavior:

- Spawns from a barrack.
- Uses shared `Soldier` behavior for movement, target finding, and attacking.
- Searches for enemies in attack range first.
- If no enemy is in attack range, searches in a larger detection range.
- If no enemy is found, moves toward the enemy base.
- Fires one `SoldierBullet` when attacking.
- Gains bonus damage and health from base level upgrades.

Important implementation details:

- Base damage is `3`.
- Damage increases with the team's base level.
- Attack timing comes from `Soldier`, which fires every 15 acts while attacking.

### Officer

Class: `Officer`

Officer is a soldier variant with a spread attack.

Current behavior:

- Uses the same shared `Soldier` targeting and movement behavior.
- Fires five `SoldierBullet` projectiles in a spread.
- Works best against clustered enemies because each attack creates multiple bullets.

### Supply Bot

Class: `SupplyBot`

Supply Bot is the package pickup unit.

Current behavior:

- Spawns from the team's base.
- Searches for active `Supply` objects in the world.
- Chooses the closest supply drop.
- Moves toward that supply drop.
- Picks it up when touching it.
- Plays a pickup sound when collecting.
- Stops collecting after reaching its current pickup limit.

Important implementation details:

- Current max supply pickups per bot: `1`.
- Cost is handled through `SupplyBot.getCost()`.
- Supply bots are also registered as team units.

## Shared Unit Behavior

### People

Class: `People`

Base class for movable units.

It currently:

- Stores movement speed.
- Adds a health bar when the unit is added to the world.
- Removes the unit if it reaches the world edge.
- Provides `moveTowards()`.
- Provides basic rotation toward movement direction.

### Soldier

Class: `Soldier`

Base class for combat soldiers such as `Marine` and `Officer`.

It currently:

- Uses three states: `idle`, `chase`, and `attack`.
- Searches for nearby enemies.
- Chases enemies within detection range.
- Moves toward the enemy base when no enemy is nearby.
- Attacks enemies inside attack range.
- Pushes nearby soldiers apart so groups do not stack as badly.
- Uses team-specific idle and recoil images.

Ranges:

- Attack range: `200`
- Detection range: `600`

## Buildings

### Base

Class: `Base`

Base is the core building for each team.

Current behavior:

- Starts with high health.
- Spawns workers through `addPeople()`.
- Spawns supply bots through `addBot()`.
- Stores and exposes its upgrade level.
- Gains more max health when upgraded.
- Triggers the end screen when destroyed.

Important implementation details:

- Base level affects marine health and damage.
- Teams store a reference to their base.

### Barrack

Class: `Barrack`

Barrack is the attack production building.

Current behavior:

- Is built by workers through team strategy logic.
- Spawns marines through `addPeople()`.
- Uses an individual marine cooldown.
- Plays an explosion sound when its health reaches zero.

Important implementation details:

- Marine creation still checks team resources.
- Barracks are tracked by `Team` after workers finish construction.

### Turret

Class: `Turret`

Turret is the defensive building.

Current behavior:

- Is built by workers through `DEF` strategy.
- Searches for enemies within range.
- Rotates toward its target.
- Fires a `SoldierBullet` periodically.
- Loses health over time through a decay timer.

Important implementation details:

- Attack range: `260`
- Fires every 60 acts while a target is present.
- Bullet damage is higher than a normal marine bullet.
- Turrets are tracked by `Team` after workers finish construction.

### Resources

Class: `Resources`

Resource nodes are the mining targets for workers.

Current behavior:

- Placed near each team's base by `MyWorld`.
- Store which team side they belong to.
- Use one of two resource images.
- Workers choose resource nodes from their own team side.

## Projectiles

### Projectile

Class: `Projectile`

Base class for bullets.

Current behavior:

- Stores shooter, damage, angle, speed, and image.
- Plays the shoot sound based on the shooter class name.
- Moves forward every act.
- Removes itself at the world edge.
- Damages the first enemy `Entity` it intersects.
- Removes itself after hitting a valid enemy.

### SoldierBullet

Class: `SoldierBullet`

Current behavior:

- Used by marines, officers, and turrets.
- Supports different constructors for speed, damage, and size.
- Uses the shared `Projectile` collision and movement behavior.

## Supply Drops

### Supply

Class: `Supply`

Supply is the package drop actor.

Current behavior:

- Spawns above the map.
- Falls to a randomized landing y-position in the play area.
- Waits after landing.
- Checks for intersecting `SupplyBot` objects.
- Applies a reward to the collecting bot's team.
- Displays floating reward text.
- Removes itself after pickup.

Reward behavior:

- Common chance: `70%`
- Rare chance: `25%`
- Ultra rare chance: remaining `5%`

Current reward values:

- Common: resources and workers.
- Rare: more resources and workers.
- Ultra rare: most resources, most workers, and one marine if the team has a barrack.

## UI And Debug Tools

### UI

Class: `UI`

Current behavior:

- Draws the bottom information panel.
- Displays resources for both teams.
- Displays worker counts.
- Displays base health.
- Displays base level.
- Displays marine health/damage summary.
- Displays current strategy.

### GameTimer

Class: `GameTimer`

Current behavior:

- Counts acts.
- Updates the displayed timer every 60 acts.
- Shows elapsed time as minutes and seconds.

### Counter

Class: `Counter`

Current behavior:

- General number-display actor.
- Currently used as an FPS counter in `MyWorld`.
- The FPS counter is placed at the top center of the world.

### EndScreen

Class: `EndScreen`

Current behavior:

- Shows the winner image when a base is destroyed.
- Stops the Greenfoot simulation.

## Setup / Configuration

### StartingWorld

Class: `StartingWorld`

Current behavior:

- Shows the title screen.
- Provides a start button.
- Moves into the setup/simulation flow.

### ConfigWorld

Class: `ConfigWorld`

Current behavior:

- Provides a pre-simulation setup screen.
- Lets the user configure team options before the match.
- Shows controls for strategy, resources, workers, supply drops, and related setup values.

### SimulationConfig

Class: `SimulationConfig`

Current behavior:

- Stores setup choices for Red and Blue.
- Stores whether supply drops are enabled.
- Provides a default config.

### TeamSetup

Class: `TeamSetup`

Current behavior:

- Stores one team's starting strategy.
- Stores starting resources.
- Stores extra workers.
- Stores extra barracks.
- Stores extra turrets.
- Stores whether supply bots are enabled.

## Asset And Performance Support

### ResourceCache

Class: `ResourceCache`

Current behavior:

- Caches file-based `GreenfootImage` objects.
- Returns image copies so scaling does not damage the cached original.
- Caches regular `GreenfootSound` objects.
- Provides a sound pool for repeated sound playback.
- Helps avoid repeated file loading during gameplay.

Use it for file-based resources such as:

- `ResourceCache.getImage("Background.png")`
- `ResourceCache.getImage(team.getName() + getClass().getName() + ".png")`
- `ResourceCache.playSound("MarineShoot.mp3", 20)`

Do not use it for generated images such as:

- Text images.
- Blank canvases.
- UI redraw buffers.

## Current Balance Notes

Current values are still being balanced.

Known examples:

- Workers are the economy backbone.
- Marines are the main attack unit.
- Turrets are short-range defensive structures.
- Supply bots create map control around supply drops.
- Base upgrades improve durability and marine strength.

If battles end too quickly, adjust damage, attack delay, or health.

If battles take too long, adjust resource income, cooldowns, production costs, or supply-drop rewards.
