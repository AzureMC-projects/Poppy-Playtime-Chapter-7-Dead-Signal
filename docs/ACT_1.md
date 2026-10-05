# Act 1 — The Signal

## Playable slice

Act 1 is an asset-free vertical slice for testing the complete opening progression before final art, animation, voice acting, and sound design are introduced.

### Sequence

1. **Arrival** — Enter Sector 07 and establish the abandoned signal-processing facility.
2. **GrabPack setup** — Visit the station and press **E** to establish the prototype link.
3. **Power routing** — Activate Node 1, Node 2, then Node 3. The security door opens after the correct sequence.
4. **Project: Echo** — Inspect the terminal and trigger Morrow's reveal.
5. **Chase** — Reach maintenance access and survive Morrow's pursuit.
6. **Ending** — Reach the far maintenance door and trigger the Act 1 completion state.

## Controls

- **WASD / Mouse** — movement and look
- **E** — interact
- **F** — flashlight
- **Esc** — return to main menu

## Known prototype limitations

- Movement currently uses jMonkeyEngine's fly camera rather than the final collision controller.
- Environment geometry is intentionally primitive.
- Morrow is a placeholder silhouette.
- There is no final animation, voice acting, music, or production sound design yet.
- The final GrabPack will become a physical first-person interaction system.
- Save/checkpoint persistence is not yet implemented.

## Definition of done

A tester should be able to start from the main menu, traverse Sector 07, activate the GrabPack station, complete all three power nodes, open the security door, inspect Project: Echo, trigger Morrow, survive the chase, reach maintenance access, and see the Act 1 ending state.
